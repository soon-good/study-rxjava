package optional;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sun.nio.sctp.IllegalReceiveException;

public class OptionalTest {

	public class Employee{
		private String name;
		public Employee (String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	@Test
	public void OptionalMap_of_map함수_테스트(){
		Employee e1 = new Employee("지드래곤");
		Optional<String> name = Optional.of(e1).map(Employee::getName);
		System.out.println(name);
	}

	@Test
	public void OptionalMap_ofNullable_map함수_테스트(){
		Optional<Employee> optEmployee = Optional.ofNullable(null);
		Optional<String> optName = optEmployee.map(Employee::getName);
		System.out.println("optName = " + optName);
	}

	@Test
	public void Optional_get메서드_테스트(){
		String test = "abc";
		String s = Optional.ofNullable(test).get();
		System.out.println("s = " + s);

		test = null;
		Optional<String> optTest1 = Optional.ofNullable(test);
		System.out.println("optTest1 = " + optTest1);
		System.out.println("optTest1.get() = " + optTest1.get());
	}

	@Test
	public void Optional_orElse확인해보기(){
		String test = null;
		Optional<String> optional = Optional.ofNullable(test);
		System.out.println("현재 optional 값 = " + optional);
		System.out.println("orElse로 값을 가져와보면 => " + optional.orElse("기본값입니다."));
	}

	@Test
	public void Optional_orElsGet_확인해보기(){
		String test = null;
		Optional<String> optional = Optional.ofNullable(test);
		System.out.println("현재 optional 값 = " + optional);
		System.out.println("orElse로 값을 가져와보면 => " + optional.orElseGet(()->"기본값이에요"));
	}

	@Test
	public void Optional_orElseThrow_테스트해보기(){
		String test = null;
		Optional<String> optional = Optional.ofNullable(test);
		System.out.println("현재 optional 값 = " + optional);
		optional.orElseThrow(()->{
			throw new IllegalReceiveException("오우, 인자값을 잘못 주셨어요!!");
		});
	}

	@Test
	public void Optional_ifPresent_테스트해보기(){
		String test1 = "ABC";
		Optional<String> optional = Optional.ofNullable(test1);
		optional.ifPresent((data)->System.out.println("data = " + data));

		String test2 = null;
		Optional.ofNullable(test2).ifPresent(data -> System.out.println("data = " + data));
	}

	@Test
	public void Optional_ifPresentOrElse_테스트해보기(){
		String test1 = "ABC";
		Optional<String> optional1 = Optional.ofNullable(test1);
		optional1.ifPresentOrElse(
			(data)->System.out.println("data = " + data),
			()-> System.out.println("비어있다ㅏㅇ ㅋㅋ"));

		String test2 = null;
		Optional<String> optional2 = Optional.ofNullable(test2);
		optional2.ifPresentOrElse(
			(data)-> System.out.println("data = " + data),
			()-> System.out.println("비어있다ㅏㅇ ㅋㅋ"));
	}

	class Person{
		private String name;
		private int age;
		public Person(){}
		public Person(String name, int age){
			this.name = name;
			this.age = age;
		}
	}

	class Car{
		private String name;
		private int price;
		public Car(){}
		public Car(String name, int price){
			this.name = name;
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public int getPrice() {
			return price;
		}
	}

	class Insurance{
		private String name;
		public Insurance(String name){
			this.name = name;
		}
	}

	public Insurance findCheapestInsurance(Person person, Car car){
		return new Insurance(car.getName() + " 교보보험");
	}

	public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car){
		if(person.isPresent() && car.isPresent()){
			return Optional.of(findCheapestInsurance(person.get(), car.get()));
		}
		else{
			return Optional.empty();
		}
	}

	@Test
	@DisplayName("테스트__가장싼보험을_출력하기_null_아닌값을_넘겨줄경우")
	public void 테스트__가장싼보험을_출력하기_null_아닌값을_넘겨줄경우(){
		Optional<Person> optPerson = Optional.ofNullable(new Person("지드래곤", 23));
		Optional<Car> optCar = Optional.ofNullable(new Car("아우디", 2000));
		Optional<Insurance> insurance = nullSafeFindCheapestInsurance(optPerson, optCar);
		System.out.println(Optional.ofNullable(insurance));
	}

	@Test
	@DisplayName("테스트__가장싼보험을_출력하기_null_을_넘겨줄경우")
	public void 테스트__가장싼보험을_출력하기_null_을_넘겨줄경우(){
		Optional<Person> optPerson = Optional.ofNullable(new Person("지드래곤", 23));
		Optional<Car> optCar = Optional.ofNullable(null);
		Optional<Insurance> insurance = nullSafeFindCheapestInsurance(optPerson, optCar);
		System.out.println(Optional.ofNullable(insurance));
	}

	public Optional<Insurance> nullSafeFindCheapestInsurance2(Optional<Person> person, Optional<Car> car){
		return person.flatMap( p -> car.map(c -> findCheapestInsurance(p, c)) );
	}

	@Test
	@DisplayName("테스트__가장싼보험을_출력하기_null_아닌값을_넘겨줄경우2")
	public void 테스트__가장싼보험을_출력하기_null_아닌값을_넘겨줄경우2(){
		Optional<Person> optPerson = Optional.ofNullable(new Person("지드래곤", 23));
		Optional<Car> optCar = Optional.ofNullable(new Car("아우디", 2000));
		Optional<Insurance> insurance = nullSafeFindCheapestInsurance2(optPerson, optCar);
		System.out.println(Optional.ofNullable(insurance));
	}

	@Test
	@DisplayName("테스트__가장싼보험을_출력하기_null_을_넘겨줄경우2")
	public void 테스트__가장싼보험을_출력하기_null_을_넘겨줄경우2(){
		Optional<Person> optPerson = Optional.ofNullable(new Person("지드래곤", 23));
		Optional<Car> optCar = Optional.ofNullable(null);
		Optional<Insurance> insurance = nullSafeFindCheapestInsurance2(optPerson, optCar);
		System.out.println(Optional.ofNullable(insurance));
	}

}
