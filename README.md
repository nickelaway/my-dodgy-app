# My Dodgy App - Static Analysis Test Application
This application has a number of security vulnerabilities deliberately built into it.
The purpose of this application is to verify that static analysis security tools find the vulnerabilities
You can find the issues by looking for the string `#Oops`, which will appear in a comment near each issue.

This application only binds to localhost so other hosts on the network cannot call the vulnerable services.

## Vulnerabilities
### Bad Random
The HashIt controller uses insecure random, and always produces the same sequence.

`http://localhost:8080/hashit?value=foo`

The first call to this will always produce an ID of: -723955400

### Shell Injection
The HashIt controller passes arbitrary input to the execution shell.

`http://localhost:8080/hashit?value=meow;cat%20/etc/passwd;echo`

### SQL Injection
The HashIt controller allows arbitrary input to be injected into SQL queries in both the `hashit` and `gethash` calls.

`http://localhost:8080/gethash?id=-723955400' or '1'='1`

### XSS
The `gethash` call renders user input in an HTML response.

The following injects some Javascript in the database:
`http://localhost:8080/hashit?value="<script>alert(1)</script>"`

The following renders it in the HTML page:
`http://localhost:8080/gethash?id=-723955400' or '1'='1`

### Bad SSL
The Proxy controller forwards HTTP requests to arbitrary hosts, ignoring SSL warnings.

The following URL displays the content from a URL with an invalid SSL configuration.
http://localhost:8080/proxy?url=https://self-signed.badssl.com/
