package org.usha.json.main;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.usha.json.main.JacksonObjectMapperExample.ToOrdersDTOMapping;
import org.usha.json.main.JacksonObjectMapperExample.ToOrdersMapping;
import org.usha.json.model.Address;
import org.usha.json.model.Customer;
import org.usha.json.model.Order;
import org.usha.json.model.OrderDTO;

public class ModelMapperExample {

	public static void main(String[] args) {
		modelMapperTest();

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
