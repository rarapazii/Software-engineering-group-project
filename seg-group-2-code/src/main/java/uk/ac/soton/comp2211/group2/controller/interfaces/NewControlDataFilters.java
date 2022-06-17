package uk.ac.soton.comp2211.group2.controller.interfaces;

import java.util.function.Predicate;

import uk.ac.soton.comp2211.group2.model.CategoryKey;

/**
 * A class containing loads of predefined filters and methods to combine them.
 * 
 * Should be fairly obvious what each filter and combiner does, but have examples anyway.
 * 
 * Examples:
 * - Under 25's only: ControlDataFilters.Age.UNDER_25
 * - Under 25 AND female: ControlDataFilters.combineAnd(ControlDataFilters.Age.UNDER_25, ControlDataFilters.Gender.FEMALE)
 * - (Under 25 AND female) OR low income: ControlDataFilters.combineOr(ControlDataFilters.Income.LOW, ControlDataFilters.combineAnd(ControlDataFilters.Age.UNDER_25, ControlDataFilters.Gender.FEMALE))
 * - Absolutely nothing: ControlDataFilters.negate(ControlDataFilters.ANY)
 */
public final class NewControlDataFilters {
	
	private NewControlDataFilters() {} //Making an instance of this class should be impossible.
	
	@SafeVarargs //Is safe as we just iterate the array: if class-cast exceptions get thrown by these then this is why.
	public static Predicate<CategoryKey> combineOr(Predicate<CategoryKey>... predicates){
		//Create a predicate that is all of these glued together with an OR. Will short-circuit as appropriate.
		
		//Check for nulls: if any nulls are present then this predicate is equal to ANY.
		for (Predicate<CategoryKey> p: predicates) {
			if (p == null) {
				return NewControlDataFilters.ANY;
			}
		}
		return (i) -> {
			for (Predicate<CategoryKey> p: predicates) {
				if (p.test(i)) {
					//Test passed, return true.
					return true;
				}
			}
			//No tests passed, return false.
			return false;
		};
	}
	
	@SafeVarargs
	public static Predicate<CategoryKey> combineAnd(Predicate<CategoryKey>... predicates){
		//As above, but creates the predicate as a logical "AND"
		
		return (i) -> {
			for (Predicate<CategoryKey> p: predicates) {
				if (p == null) {
					continue; //ANY, so always true.
				}
				else if (!p.test(i)) {
					//Test failed, return false.
					return false;
				}
			}
			//No tests failed, return true.
			return true;
		};
	}
	
	public static Predicate<CategoryKey> negate(Predicate<CategoryKey> p){
		if (p == null) {
			//Negation of 'ANY' is nothing
			return (i) -> {return false;};
		}
		else {
			return p.negate();
		}
	}
	
	public static final Predicate<CategoryKey> ANY = null;
	
	/**
	 * Note about Age specifically: the x before the age ranges is intentional, as variable names can't start with a number.
	 */
	public static final class Age {
		private Age() {}
		
		public static final Predicate<CategoryKey> UNDER_25 = (i) -> {
			return i.getAge() == uk.ac.soton.comp2211.group2.controller.Age.UNDER_25;
		};
		
		public static final Predicate<CategoryKey> x25_TO_34 = (i) -> {
			return i.getAge() == uk.ac.soton.comp2211.group2.controller.Age.x25_TO_34;
		};
		
		public static final Predicate<CategoryKey> x35_TO_44 = (i) -> {
			return i.getAge() == uk.ac.soton.comp2211.group2.controller.Age.x35_TO_44;
		};
		
		public static final Predicate<CategoryKey> x45_TO_54 = (i) -> {
			return i.getAge() == uk.ac.soton.comp2211.group2.controller.Age.x45_TO_54;
		};
		
		public static final Predicate<CategoryKey> OVER_54 = (i) -> {
			return i.getAge() == uk.ac.soton.comp2211.group2.controller.Age.OVER_55;
		};
	}
	
	public static final class Gender {
		private Gender() {}
		
		public static final Predicate<CategoryKey> MALE = (i) -> {
			return i.getGender() == uk.ac.soton.comp2211.group2.controller.Gender.MALE;
		};
		
		public static final Predicate<CategoryKey> FEMALE = (i) -> {
			return i.getGender() == uk.ac.soton.comp2211.group2.controller.Gender.FEMALE;
		};
	}
	
	public static final class Income {
		private Income() {}
		
		public static final Predicate<CategoryKey> LOW = (i) -> {
			return i.getIncome() == uk.ac.soton.comp2211.group2.controller.Income.LOW;
		};
		
		public static final Predicate<CategoryKey> MEDIUM = (i) -> {
			return i.getIncome() == uk.ac.soton.comp2211.group2.controller.Income.MEDIUM;
		};
		
		public static final Predicate<CategoryKey> HIGH = (i) -> {
			return i.getIncome() == uk.ac.soton.comp2211.group2.controller.Income.HIGH;
		};
	}
	
	public static final class Context {
		private Context() {}
		
		public static final Predicate<CategoryKey> BLOG = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.BLOG;
		};
		
		public static final Predicate<CategoryKey> NEWS = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.NEWS;
		};
		
		public static final Predicate<CategoryKey> SHOPPING = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.SHOPPING;
		};
		
		public static final Predicate<CategoryKey> SOCIAL_MEDIA = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.SOCIAL_MEDIA;
		};
		
		public static final Predicate<CategoryKey> HOBBIES = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.HOBBIES;
		};
		
		public static final Predicate<CategoryKey> TRAVEL = (i) -> {
			return i.getContext() == uk.ac.soton.comp2211.group2.controller.Context.TRAVEL;
		};
	}
}
