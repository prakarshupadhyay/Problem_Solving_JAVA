public class CompanyInformationUsingSTATIC {
    static class Employee {
        static String companyName = "Tech Solutions Inc.";
        static String companyAddress = "123 Tech Avenue, Silicon Valley, CA";
        static String companyContact = "(123) 456-7890";

        String employeeName;
        int employeeId;

        Employee(String employeeName, int employeeId) {
            this.employeeName = employeeName;
            this.employeeId = employeeId;
        }

        void displayEmployeeInfo() {
            System.out.println("Employee Name: " + employeeName);
            System.out.println("Employee ID: " + employeeId);
            System.out.println("Company Name: " + companyName);
            System.out.println("Company Address: " + companyAddress);
            System.out.println("Company Contact: " + companyContact);
        }
    }
    public static class CompanyInformation{
        public static void main(String[] args) {
            Employee employee1 = new Employee("Alice Johnson", 101);
            Employee employee2 = new Employee("Bob Smith", 102);

            System.out.println("Employee 1 Information:");
            employee1.displayEmployeeInfo();

            System.out.println("\nEmployee 2 Information:");
            employee2.displayEmployeeInfo();
        }
    }
}
