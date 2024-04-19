package org.keycloak.benchmark.crossdc.jmx;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.*;

/**
 * Helper class which does all stuff, i.e. connecting to MBean server, performs JMX operation.
 */
public class Helper {

    /**
     * The map keeping instances for each provided URL, so instance is created once and kept it for further operations.
     */
    private static Map<String, Helper> instanceMap = new HashMap<>();
    /**
     * The JMX service URL.
     */
    private String jmxServiceUrl;
    /**
     * The username for connecting JMX.
     */
    private String userName;
    /**
     * Password for creating JMX.
     */
    private String password;
    /**
     * Instance of gained connection.
     */
    private MBeanServerConnection mBeanServerConnection;

    /**
     * Method for getting instance of MBean connection based on the passed parameters.
     * @param jmxServiceUrl     the service URL;
     * @param userName          the userName for connecting to JMX;
     * @param password          the password for connectin to JMX;
     * @return                  returns instance of the class associated with the provided JMX servcie URL.
     */
    public static Helper getInstance(String jmxServiceUrl, String userName, String password) {
        Helper instance = instanceMap.get(jmxServiceUrl);
        if (instance == null) {
            instance = new Helper(jmxServiceUrl, userName, password);
            instanceMap.put(jmxServiceUrl, instance);
        }
        return instance;
    }

    private Helper(String jmxServiceUrl, String userName, String password) {
        this.jmxServiceUrl = jmxServiceUrl;
        this.userName = userName;
        this.password = password;
        initializeMBeanServerConnection();
    }
    /**
     * Creates JMXConnector object from the provided jmx service URL.
     */
    private void initializeMBeanServerConnection() {
        try {
            JMXServiceURL url = new JMXServiceURL(jmxServiceUrl);
            Map<String, String[]> env = new HashMap<>();
            String[] credentials = { userName, password };
            env.put(JMXConnector.CREDENTIALS, credentials);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(url, env);
            mBeanServerConnection= jmxConnector.getMBeanServerConnection();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Runs the MBean query and returns MBean Object associated with passed parameters.
     * @param query         the query to MBean.
     * @return the retrieved MBean Object associated with passed parameters.
     * @throws MalformedObjectNameException
     * @throws IOException
     */
    private Collection<ObjectInstance> getQueryResult(String query) throws MalformedObjectNameException, IOException {
        return mBeanServerConnection.queryMBeans(new ObjectName(query), null);
    }

    /**
     * Performs JMX operation based on the passed query, operation name and parameters.
     * @param objectInstance        the object retrieved based on the passed MBean query.
     * @param targetName            the name of the operation;
     * @param methodParameters      the Array with parameter values which should be passed to JMX Operation;
     * @param methodSignatures      the Array with signatures for each parameter (so the length of methodParameters and
     *                              methodSignatures should be the same);
     * @return                      the retrieved MBean Object associated with passed parameters.
     * @throws Exception
     */
    private Object performJmxOperation(ObjectInstance objectInstance, String targetName,
                                      String[] methodParameters, String[] methodSignatures) throws Exception {
        return mBeanServerConnection.invoke(objectInstance.getObjectName(), targetName, methodParameters, methodSignatures);
    }

    /**
     * Returns the value of  JMX attribute based on the passed query and attribute name.
     * @param query                 the query of object to be retrieved .
     * @param targetName            the name of the attribute;
     * @return                      the retrieved value of the attribute.
     * @throws Exception
     */
    public Object[] getAttributeValue(String query, String targetName) {
        try {
            Object[] values = {};
            Collection<ObjectInstance> queriedObjects = getQueryResult(query);
            int i = 0;
            for (ObjectInstance object : queriedObjects) {
                values[i++] = mBeanServerConnection.getAttribute(object.getObjectName(), targetName);
            }
            return values;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return new String[]{"Something went wrong"};
    }

    /**
     * Invokes JMX Operation based on the passed parameters.
     * @param query             the query of the Managed Bean;
     * @param targetName        the name of the operation to run over JMX;
     * @param methodParameters  the Array with parameter values which should be passed to JMX Operation;
     * @param methodSignatures  the Array with signatures for each parameter (so the length of methodParameters and
     *                          methodSignatures should be the same);
     * @return      result of the performed operation.
     */
    public List<String> invokeJmxOperation(String query, String targetName,
                                     String[] methodParameters, String[] methodSignatures) {
        List<String> results = new ArrayList<>();
        try {
            Collection<ObjectInstance> queriedObjects = getQueryResult(query);

            for (ObjectInstance jmxObject : queriedObjects) {
                Object result = performJmxOperation(jmxObject, targetName, methodParameters, methodSignatures);
                if (result != null) {
                    results.add(result.toString());
                }
            }
            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        results.add("Something went wrong");
        return results;
    }

    /**
     * Prints all MBeans for the specified URL in the log.
     */
    public void printMBeans() {
        try {
            Set mySet = mBeanServerConnection.queryNames(new ObjectName("*:*"), null);
            Iterator it = mySet.iterator();
            while (it.hasNext()) {
                ObjectName myName = (ObjectName) it.next();
                try {
                    System.out.println("--> " + myName.getCanonicalName());
                    // get all attributes
                    MBeanAttributeInfo[] atribs = mBeanServerConnection.getMBeanInfo(myName).getAttributes();
                    for (int i = 0; i < atribs.length; i++)  {
                        System.out.println("         Attribute: " + atribs[i].getName() +
                                "   of Type : " + atribs[i].getType());
                    }
                    // get all operations
                    MBeanOperationInfo[] operations =
                            mBeanServerConnection.getMBeanInfo(myName).getOperations();
                    for (int i = 0; i < operations.length; i++)  {
                        System.out.print("         Operation: " +
                                operations[i].getReturnType() + "  " +
                                operations[i].getName() + "(");
                        for (int j = 0; j < operations[i].getSignature().length;j++)
                            System.out.print(operations[i].getSignature()[j].
                                    getName() + ":" +
                                    operations[i].getSignature()[j].
                                            getType() + "  ");
                        System.out.println(")");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
