package com.company.retail.models;
/**
 * @author omkar
 * @Description pojo to hold json  response of Google Distance Matrix API
 * Dummy URI : https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&
 * origins=40.6655101,-73.89188969999998&
 * destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592&
 * key=AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw
 */

public class DistanceMatrixModel {
	private String status;

	private String[] destination_addresses;

	private String[] origin_addresses;

	private Rows[] rows;

	public DistanceMatrixModel() {	}

	public DistanceMatrixModel(String status, String[] destination_addresses, String[] origin_addresses, Rows[] rows) {
		super();
		this.status = status;
		this.destination_addresses = destination_addresses;
		this.origin_addresses = origin_addresses;
		this.rows = rows;
	}

	public static class Rows
	{
		private Elements[] elements;

		public Rows() {}

		public Rows(Elements[] elements) {
			super();
			this.elements = elements;
		}

		public Elements[] getElements ()
		{
			return elements;
		}

		public void setElements (Elements[] elements)
		{
			this.elements = elements;
		}

		@Override
		public String toString()
		{
			return "Rows [elements = "+elements.toString()+"]";
		}
	}

	public String getStatus ()
	{
		return status;
	}

	public void setStatus (String status)
	{
		this.status = status;
	}

	public String[] getDestination_addresses ()
	{
		return destination_addresses;
	}

	public void setDestination_addresses (String[] destination_addresses)
	{
		this.destination_addresses = destination_addresses;
	}

	public String[] getOrigin_addresses ()
	{
		return origin_addresses;
	}

	public void setOrigin_addresses (String[] origin_addresses)
	{
		this.origin_addresses = origin_addresses;
	}

	public Rows[] getRows ()
	{
		return rows;
	}

	public void setRows (Rows[] rows)
	{
		this.rows = rows;
	}

	@Override
	public String toString()
	{
		return "DistanceMatrixModel [status = "+status+", destination_addresses = "+destination_addresses.length+", origin_addresses = "+origin_addresses.length+", rows = "+rows.toString()+"]";
	}

}
