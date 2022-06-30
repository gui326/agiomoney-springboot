package gg.agiomoney.repository.helper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import gg.agiomoney.model.Loan;

public class LoanQueriesImpl implements LoanQueries{

	private static final Logger logger = LoggerFactory.getLogger(LoanQueriesImpl.class);
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional
	public Page<Loan> findLoansByClientIdPaginacao(Long id, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Loan> criteriaQuery = builder.createQuery(Loan.class);
		Root<Loan> c = criteriaQuery.from(Loan.class);
		
		TypedQuery<Loan> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(c.<Long>get("client"), id));

		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		Order order = builder.asc(c.<String>get("state"));
		criteriaQuery.select(c).where(predArray).distinct(true).orderBy(order);
		typedQuery = manager.createQuery(criteriaQuery);
		typedQuery.setHint(QueryHints.PASS_DISTINCT_THROUGH, false);
		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalRegistrosPorPagina);

		List<Loan> loans = typedQuery.getResultList();
		
		long totalRegistros = getTotalContatos(builder, predArray); 
		
		Page<Loan> pagina = new PageImpl<>(loans, pageable, totalRegistros);

		return pagina;
	}
	
	@Override
	@Transactional
	public Page<Loan> findLoansByCompanyIdPaginacao(Long id, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Loan> criteriaQuery = builder.createQuery(Loan.class);
		Root<Loan> c = criteriaQuery.from(Loan.class);
		
		TypedQuery<Loan> typedQuery;
		List<Predicate> predicateList = new ArrayList<>();

		predicateList.add(builder.equal(c.<Long>get("company"), id));

		Predicate[] predArray = new Predicate[predicateList.size()];
		predicateList.toArray(predArray);

		Order order = builder.asc(c.<String>get("state"));
		criteriaQuery.select(c).where(predArray).distinct(true).orderBy(order);
		typedQuery = manager.createQuery(criteriaQuery);
		typedQuery.setHint(QueryHints.PASS_DISTINCT_THROUGH, false);
		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalRegistrosPorPagina);

		List<Loan> loans = typedQuery.getResultList();
		
		long totalRegistros = getTotalContatos(builder, predArray); 
		
		Page<Loan> pagina = new PageImpl<>(loans, pageable, totalRegistros);

		return pagina;
	}
	
	private long getTotalContatos(CriteriaBuilder builder, Predicate[] predArray) {
		CriteriaQuery<Long> criteriaQueryTotal = builder.createQuery(Long.class);
		Root<Loan> cTotal = criteriaQueryTotal.from(Loan.class);
		criteriaQueryTotal.select(builder.count(cTotal)).where(predArray).distinct(true);
		TypedQuery<Long> typedQueryTotal = manager.createQuery(criteriaQueryTotal);
		typedQueryTotal.setHint(QueryHints.PASS_DISTINCT_THROUGH, false);
		long totalRegistros = typedQueryTotal.getSingleResult();
		return totalRegistros;
	}
}
