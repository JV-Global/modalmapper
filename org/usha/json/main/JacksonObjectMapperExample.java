package org.usha.json.main;



import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.usha.json.model.Address;
import org.usha.json.model.Customer;
import org.usha.json.model.Employee;
import org.usha.json.model.Order;
import org.usha.json.model.OrderDTO;


public class JacksonObjectMapperExample {

	public static void main(String[] args) throws IOException {
		modelMapperTest();
		
		
		jsonMapper();
	}

	private static void jsonMapper()
			throws IOException, JsonParseException, JsonMappingException, JsonGenerationException {
		//read json file data to String
		byte[] jsonData = Files.readAllBytes(Paths.get("C:\\TEST\\employee.txt"));
		
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		
		//convert json string to object
		Employee emp = objectMapper.readValue(jsonData, Employee.class);
		
		System.out.println("Employee Object\n"+emp);
		
		//convert Object to json string
		Employee emp1 = createEmployee();
		//configure Object mapper for pretty print
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		
		//writing to console, can write to any output stream such as file
		StringWriter stringEmp = new StringWriter();
		objectMapper.writeValue(stringEmp, emp1);
		System.out.println("Employee JSON is\n"+stringEmp);
	}

	private static void modelMapperTest() {
		Order order = createData();		
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new ToOrdersMapping());
		modelMapper.addMappings(new ToOrdersDTOMapping());

		OrderDTO dto = modelMapper.map(order, OrderDTO.class);
		assert dto.getCustomerName().equals(order.getCustomer().getName());
		assert dto.getShippingStreetAddress().equals(order.getShippingAddress().getStreet());
		assert dto.getShippingCity().equals(order.getShippingAddress().getCity());
		assert dto.getBillingStreetAddress().equals(order.getBillingAddress().getStreet());
		assert dto.getBillingCity().equals(order.getBillingAddress().getCity());
		
		
		Order order1 = modelMapper.map( dto,Order.class);
		assert dto.getCustomerName().equals(order1.getCustomer().getName());
		assert dto.getShippingStreetAddress().equals(order1.getShippingAddress().getStreet());
		assert dto.getShippingCity().equals(order1.getShippingAddress().getCity());
		assert dto.getBillingStreetAddress().equals(order1.getBillingAddress().getStreet());
		assert dto.getBillingCity().equals(order1.getBillingAddress().getCity());
	}

	private static Order createData() {
		Order order= new Order();		
		
		Customer cust=new Customer();
		cust.setName("testcust");
		order.setCustomer(cust);
		
		Address billAddr=new Address();
		billAddr.setCity("Calverton");
		billAddr.setStreet("powdermill");
		billAddr.setZipcode(22222);
		order.setBillingAddress(billAddr);
		
		Address shipAddr=new Address();
		shipAddr.setCity("Calverton1");
		shipAddr.setStreet("powdermill1");
		shipAddr.setZipcode(22223);
		order.setShippingAddress(shipAddr);
		return order;
	}
	
	public static Employee createEmployee() {

		Employee emp = new Employee();
		emp.setId(100);
		emp.setName("David");
		emp.setPermanent(false);
		emp.setPhoneNumbers(new long[] { 123456, 987654 });
		emp.setRole("Manager");

		Address add = new Address();
		add.setCity("Bangalore");
		add.setStreet("BTM 1st Stage");
		add.setZipcode(560100);
		emp.setAddress(add);

		List<String> cities = new ArrayList<String>();
		cities.add("Los Angeles");
		cities.add("New York");
		emp.setCities(cities);

		Map<String, String> props = new HashMap<String, String>();
		props.put("salary", "1000 Rs");
		props.put("age", "28 years");
		emp.setProperties(props);

		return emp;
	}
	static class ToOrdersMapping extends PropertyMap<Order, OrderDTO> {
	    /** Define mapping configure PropertyMap <Source, Destination> **/
	    @Override
	    protected void configure() {
	        map().setBillingCity(source.getBillingAddress().getCity());
	        map().setBillingStreetAddress(source.getBillingAddress().getStreet());
	        map().setShippingCity(source.getShippingAddress().getCity());
	        map().setShippingStreetAddress(source.getShippingAddress().getStreet());
	        
	    }
	}

	static class ToOrdersDTOMapping extends PropertyMap<OrderDTO, Order> {
	   /** Define mapping configure PropertyMap <Source, Destination> **/
	   @Override
	   protected void configure() {
	       map().getCustomer().setName(source.getCustomerName());
	       map().getBillingAddress().setCity(source.getBillingCity());
	       map().getBillingAddress().setStreet(source.getBillingStreetAddress());
	       map().getShippingAddress().setCity(source.getShippingCity());
	       map().getShippingAddress().setStreet(source.getShippingStreetAddress());
	   }
	}

}