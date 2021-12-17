package in.co.everyrupee.pojo.user;

public enum AccountCategories {
  ASSET("Asset"),
  LIABILITY("Liability"),
  ALL("All");

  private String type;

  private AccountCategories(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
