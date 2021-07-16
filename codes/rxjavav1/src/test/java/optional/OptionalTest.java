package optional;

import java.util.Optional;

import org.junit.jupiter.api.Test;

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
}
