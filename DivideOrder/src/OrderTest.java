public class OrderTest {
	private Integer orderLevelSort;
	private String orderNo;
	private String orderLevel;

	private OrderTest(Builder builder) {
		setOrderLevelSort(builder.orderLevelSort);
		setOrderNo(builder.orderNo);
		setOrderLevel(builder.orderLevel);
	}

	public Integer getOrderLevelSort() {
		return orderLevelSort;
	}

	public void setOrderLevelSort(Integer orderLevelSort) {
		this.orderLevelSort = orderLevelSort;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderLevel() {
		return orderLevel;
	}

	public void setOrderLevel(String orderLevel) {
		this.orderLevel = orderLevel;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static final class Builder {
		private Integer orderLevelSort;
		private String orderNo;
		private String orderLevel;

		public Builder() {
		}

		public Builder orderLevelSort(Integer val) {
			orderLevelSort = val;
			return this;
		}

		public Builder orderNo(String val) {
			orderNo = val;
			return this;
		}

		public Builder orderLevel(String val) {
			orderLevel = val;
			return this;
		}

		public OrderTest build() {
			return new OrderTest(this);
		}
	}

	@Override
	public String toString() {
		return "OrderTest{" + "orderLevelSort=" + orderLevelSort
				+ ", orderNo='" + orderNo + '\'' + ", orderLevel='"
				+ orderLevel + '\'' + '}';
	}

}
