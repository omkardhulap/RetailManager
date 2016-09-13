package com.company.retail.models;
/**
 * @author omkar
 */

import javax.validation.constraints.NotNull;

public class Shop {
	
	@NotNull
    protected String shopName;
    
    @NotNull
    protected ShopAddress shopAddress;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public ShopAddress getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(ShopAddress shopAddress) {
        this.shopAddress = shopAddress;
    }

    @Override
	public String toString() {
		return "Shop [shopName=" + shopName + ", shopAddress=" + shopAddress + "]";
	}

	public static class ShopAddress {
		
        @NotNull
        private String number;

        @NotNull
        private int postCode;
        
        private Location location;

        public ShopAddress(){}
        
        public ShopAddress(String number, int postCode) {
            this.number = number;
            this.postCode = postCode;
        }
        
        public ShopAddress(String number, int postCode, Location location) {
			super();
			this.number = number;
			this.postCode = postCode;
			this.location = location;
		}

		public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getPostCode() {
            return postCode;
        }

        public void setPostCode(int postCode) {
            this.postCode = postCode;
        }

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		@Override
		public String toString() {
			return "ShopAddress [number=" + number + ", postCode=" + postCode + ", location=" + location + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((location == null) ? 0 : location.hashCode());
			result = prime * result + ((number == null) ? 0 : number.hashCode());
			result = prime * result + postCode;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ShopAddress other = (ShopAddress) obj;
			if (location == null) {
				if (other.location != null)
					return false;
			} else if (!location.equals(other.location))
				return false;
			if (number == null) {
				if (other.number != null)
					return false;
			} else if (!number.equals(other.number))
				return false;
			if (postCode != other.postCode)
				return false;
			return true;
		}

    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((shopAddress == null) ? 0 : shopAddress.hashCode());
		result = prime * result + ((shopName == null) ? 0 : shopName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shop other = (Shop) obj;
		if (shopAddress == null) {
			if (other.shopAddress != null)
				return false;
		} else if (!shopAddress.equals(other.shopAddress))
			return false;
		if (shopName == null) {
			if (other.shopName != null)
				return false;
		} else if (!shopName.equals(other.shopName))
			return false;
		return true;
	}
	
}
