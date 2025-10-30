public enum Category {
    SALARY, FREELANCE, INVESTMENT, OTHER_INCOME,
    FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, SHOPPING, RENT, OTHER_EXPENSE;

    public static Category[] incomeCategories() {
        return new Category[]{SALARY, FREELANCE, INVESTMENT, OTHER_INCOME};
    }
    public static Category[] expenseCategories() {
        return new Category[]{FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, SHOPPING, RENT, OTHER_EXPENSE};
    }
}
