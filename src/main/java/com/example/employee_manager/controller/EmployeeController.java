package com.example.employee_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 1. Display list of employees (Read & Search)
    @GetMapping({"/", "/employees"})
    public String viewHomePage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("employees", employeeService.searchEmployees(keyword));
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // 2. Show form to add a new employee (Create)
    @GetMapping("/employees/new")
    public String showNewEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "new_employee";
    }

    // 3. Save or update employee with validation (Create / Update)
    // Uses Post-Redirect-Get pattern to prevent duplicate form submissions on refresh
    @PostMapping("/employees/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result) {
        if (result.hasErrors()) {
            return employee.getId() == null ? "new_employee" : "edit_employee";
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // 4. Show form to edit an existing employee (Update)
    @GetMapping("/employees/edit/{id}")
    public String showEditEmployeeForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "edit_employee";
    }

    // 5. Delete an employee by ID (Delete)
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/employees";
    }
}
