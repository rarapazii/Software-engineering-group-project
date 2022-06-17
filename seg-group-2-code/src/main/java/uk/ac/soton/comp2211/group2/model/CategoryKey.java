package uk.ac.soton.comp2211.group2.model;

import java.time.LocalDateTime;

import uk.ac.soton.comp2211.group2.controller.Age;
import uk.ac.soton.comp2211.group2.controller.Context;
import uk.ac.soton.comp2211.group2.controller.Gender;
import uk.ac.soton.comp2211.group2.controller.Income;


public class CategoryKey implements Comparable<CategoryKey> {
	private final LocalDateTime start;
	private final Age age;
	private final Gender gender;
	private final Context context;
	private final Income income;
	
	CategoryKey(LocalDateTime start, Age age, Gender gender, Context context, Income income){
		this.start = start;
		this.age = age;
		this.gender = gender;
		this.context = context;
		this.income = income;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CategoryKey)) {
			return false;
		}
		else {
			CategoryKey ck = (CategoryKey) o;
			return this.start.isEqual(ck.start) && this.context == ck.context && this.age == ck.age && this.income == ck.income && this.gender == ck.gender;
		}
	}

	@Override
	public int compareTo(CategoryKey other) {
		int dateCmp = this.start.compareTo(other.start);
		if (dateCmp != 0) {
			return dateCmp;
		}
		int cCmp = this.context.compareTo(other.context);
		if (cCmp != 0) {
			return cCmp;
		}
		int aCmp = this.age.compareTo(other.age);
		if (aCmp != 0) {
			return aCmp;
		}
		int iCmp = this.income.compareTo(other.income);
		if (iCmp != 0) {
			return iCmp;
		}
		return this.gender.compareTo(other.gender);
	}

	/**
	 * @return the start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * @return the age
	 */
	public Age getAge() {
		return age;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @return the income
	 */
	public Income getIncome() {
		return income;
	}
	
	@Override
	public String toString() {
		return String.format("Time: %s, Age: %s, Gender: %s, Context: %s, Income: %s", this.start.toString(), this.age.toString(), this.gender.toString(), this.context.toString(), this.income.toString());
	}
}
