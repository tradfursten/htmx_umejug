package se.umejug.exchangeday;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import se.umejug.exchangeday.models.ExchangeDay;

public interface ExchangeDayRepository extends CrudRepository<ExchangeDay, Long> {

    List<ExchangeDay> findAll();
}
