package com.example.christopher.recycler;

public class Employee {

    private String name;
    private long empId;
    private String department;

    private static int ctr = 1;

    public Employee() {
        this.name = "Employee Name " + ctr;
        this.empId = System.currentTimeMillis();
        this.department = "Department " + ctr;
        ctr++;
    }

    public String getName() {
        return name;
    }

    public long getEmpId() {
        return empId;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return name + " (" + empId+ "), " + department;
    }
}