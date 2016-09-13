package com.company.retail.models;
/**
 * @author omkar
 * @Description pojo to hold json Google Distance Matrix API
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

		public static class Elements
        {
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
                    return "ClassPojo [text = "+text+", value = "+value+"]";
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
                    return "ClassPojo [text = "+text+", value = "+value+"]";
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
                return "ClassPojo [duration = "+duration+", distance = "+distance+", status = "+status+"]";
            }
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
            return "Rows [elements = "+elements+"]";
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
        return "DistanceMatrixModel [status = "+status+", destination_addresses = "+destination_addresses+", origin_addresses = "+origin_addresses+", rows = "+rows+"]";
    }
	
}
