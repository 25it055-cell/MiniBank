package minibank.model;

import minibank.model.annotation.Id;
import minibank.model.annotation.MaxLength;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;

    @Id
    private final String customerId;

    @MaxLength(50)
    private String name;
    private String email;
    private String mobile;
    private Address address;

    private static synchronized String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public Customer(String name, String email, String mobile, Address address) {
        this(name, email, mobile);
        this.address = address;
    }

    public Customer(String customerId, String name, String email, String mobile, Address address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public Customer clone() {
        try {
            Customer cloned = (Customer) super.clone();
            if (this.address != null) {
                cloned.address = this.address.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                (address != null ? ", address=" + address : "") +
                '}';
    }

    public static class Address implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;
        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError("Clone not supported", e);
            }
        }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }
}
