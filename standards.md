# Project Code Standards and Best Practices 

## Global Naming Conventions

1. **Variables**
    You must name your variables something readable and fitting.
    We are using **camelCase** variable naming. No snake case.
    Don't do:
        ```int x = 5;```
        ```int age_variable = 5;```
    Do:
        ```int age = 5;```
       ```int userAge = 5;```

2. **Classes**
    Classes must be named according to the UML plan. Classes must be named using **PascalCase**.
    Don't do:
        ```public class qrCode```
    Do:
        ```public class QrCode```
3. **Order class variables by scope**
    To make things look clean and organized, we want to organize class attributes by their scope (public, protected, private)
    Example:
    ```
    private int age;
    private String name;
    private String address;
    
    public String alias;
    public int idNumber;

    protected String department;
    protected int sectionNumber;
    ```

4. **Using Underscores in Numeric Literals**
    When we are hard-coding a large number, we want it to be readable. So to make numbers readable, we will separate the digits with underscores.
    Don't do:
    ```public int bigNumber = 10000000000;```
    Do:
    ```public int bigNumber = 10_000_000_000;```

5. **Spell things right**
    You're all University students, spell shit right. Don't make acronyms or shorten any words unless it is blatantly obvious. Remember: **you are not the only one who will read your code**.

6. **Do not initialize class variables**
    Java already does this for you, you don't have to initialize anything.
    Don't do:
    ```
    public int age = 0;
    public String name = null;
    public Double cash = 0.0;
    ```
    Do:
    ```
    public int age;
    public String name;
    public Double cash;
    ```
    **HOWEVER IF YOU ARE WORKING WITH A COLLECTION, INITIALIZE IT**
    Example:
    ```
    ArrayList<User> userList = new ArrayList<User>;
    List<City> cityList = new List<City>;
    ```
7. **Use boolean and not Boolean**
    Using ```Boolean``` is outdated and it forces us to do ```variable.booleanValue``` to access the value. Instead, use ```boolean``` which allows us to use the variable directly when evaluating boolean statements.

## Comments
It is important to comment our code to ensure that all members of the team understand what your code is doing and why it is needed.
If your code is not readable or too much logic is going on, break the code up into "chunks" to allow for better readability.
Example:
```
\\ this function validates an incoming 
\\ user object and sends to to the database.
...
\\ validate user object
if (!user.valid) {
    return false;
}
...

\\ send user object to db
db.send(user);
...
```

### Loops
Commenting large loops that are hard to track is important so everyone knows *what* your code does. It can also help you optimize your code and remove redundancy.

Example:
```
\\ This loop takes in a user object and increments their age, 
\\ changes their last name to "Johnson" and makes their department
\\ id eqaul to 1.
\\ Example:
\\ BEFORE
\\ Employee {
\\    name = "Jay Kuang",
\\    age = 25,
\\    deptID = 12
\\ }
\\ AFTER:
\\ Employee {
\\    name = "Jay Johnson",
\\    age = 26,
\\    deptID = 1
\\ }
for (employee in employees) {
    employee.age = employee.age + 1;
    employee.lastName = "Johnson";
    employee.deptID = 1;
}
```
## Functions
Similar to loops, functions should be named accordingly and should have the same "example" structure as loops if it is dealing with an object and changing it. Each function should also have a javadoc that meets all the requirements of a javadoc.

## Know when to make a function
If you notice one of your functions is getting a *bit* too long and you notice yourself repeating things too often, it is best to write a function. Especially if you are executing a function within a loop, this will allow you to bypass the loop comment as the helper function can explain what happens.

