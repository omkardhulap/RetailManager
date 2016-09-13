package com.company.retail.models;
/**
 * @author omkar
 * @Description pojo to hold json response of Google Distance Matrix API
 */

public class Elements {

	private Duration duration;

	private Distance distance;

	private String status;

	public Elements() { }

	public Elements(Duration duration, Distance distance, String status) {
		super();
		this.duration = duration;
		this.distance = distance;
		this.status = status;
	}

	public static class Distance
	{
		private String text;

		private String value;

		public Distance() { }

		public Distance(String text, String value) {
			super();
			this.text = text;
			this.value = value;
		}

		public String getText ()
		{
			return text;
		}

		public void setText (String text)
		{
			this.text = text;
		}

		public String getValue ()
		{
			return value;
		}

		public void setValue (String value)
		{
			this.value = value;
		}

		@Override
		public String toString()
		{
			return "Distance [text = "+text+", value = "+value+"]";
		}
	}

	public static class Duration
	{
		private String text;

		private String value;

		public Duration() { }

		public Duration(String text, String value) {
			super();
			this.text = text;
			this.value = value;
		}

		public String getText ()
		{
			return text;
		}

		public void setText (String text)
		{
			this.text = text;
		}

		public String getValue ()
		{
			return value;
		}

		public void setValue (String value)
		{
			this.value = value;
		}

		@Override
		public String toString()
		{
			return "Duration [text = "+text+", value = "+value+"]";
		}
	}

	public Duration getDuration ()
	{
		return duration;
	}

	public void setDuration (Duration duration)
	{
		this.duration = duration;
	}

	public Distance getDistance ()
	{
		return distance;
	}

	public void setDistance (Distance distance)
	{
		this.distance = distance;
	}

	public String getStatus ()
	{
		return status;
	}

	public void setStatus (String status)
	{
		this.status = status;
	}

	@Override
	public String toString()
	{
		return "Elements [duration = "+duration.toString()+", distance = "+distance.toString()+", status = "+status+"]";
	}

}
