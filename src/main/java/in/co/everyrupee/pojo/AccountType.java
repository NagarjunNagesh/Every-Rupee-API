package in.co.everyrupee.pojo;

public enum AccountType {
	ASSET("Asset"), LIABILITY("Liability");

	private String type;

	private AccountType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
