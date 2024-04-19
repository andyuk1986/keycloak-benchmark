This is a simple java application for performing jmx calls.
The application works on embedded Tomcat server, receives only HTTP calls with POST method. The endpoint receives parameters
in JSON format, which includes:
`jmxServiceUrl` - The JMX URL to which the MBeanServerConnection should be gained.
`query`: The query to MBean.
`targetName` : The name of the operation to be called.
`userName` and `password` credentials connected with server to which JMX you need to connect.

For building the executable jar, run the following command:
`mvn clean package`

There is a `Dockerfile` included for building the image if necessary.
