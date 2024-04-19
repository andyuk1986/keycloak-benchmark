package org.keycloak.benchmark.crossdc.jmx;

import java.util.Arrays;
import java.util.Objects;

/**
 * The Params class is the Java Bean representing the parameters which come in JSON format to HTTP endpoint.
 */
public class Params {
    /**
     * JMX Service URL
     */
    private String jmxServiceUrl;
    /**
     * Username for connecting to the service.
     */
    private String userName;
    /**
     * Password for connecting to the password.
     */
    private String password;
    /**
     * The name of the operation that should run.
     */
    private String targetName;
    /**
     * The query that should be run for picking the Managed Bean.
     */
    private String query;
    /**
     * The parameters value array needed for passing to operation.
     */
    private String[] parameterValues;
    /**
     * The parameters signature array.
     */
    private String[] parameterSignatures;

    public Params(String jmxServiceUrl, String userName, String password, String targetName, String query, String[] parameterValues, String[] parameterSignatures) {
        this.jmxServiceUrl = jmxServiceUrl;
        this.userName = userName;
        this.password = password;
        this.targetName = targetName;
        this.query = query;
        this.parameterValues = parameterValues;
        this.parameterSignatures = parameterSignatures;
    }

    public String getJmxServiceUrl() {
        return jmxServiceUrl;
    }

    public void setJmxServiceUrl(String jmxServiceUrl) {
        this.jmxServiceUrl = jmxServiceUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(String[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public String[] getParameterSignatures() {
        return parameterSignatures;
    }

    public void setParameterSignatures(String[] parameterSignatures) {
        this.parameterSignatures = parameterSignatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Params params = (Params) o;
        return jmxServiceUrl.equals(params.jmxServiceUrl) && userName.equals(params.userName) && password.equals(params.password) && targetName.equals(params.targetName) && query.equals(params.query) && Arrays.equals(parameterValues, params.parameterValues) && Arrays.equals(parameterSignatures, params.parameterSignatures);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(jmxServiceUrl, userName, password, targetName, query);
        result = 31 * result + Arrays.hashCode(parameterValues);
        result = 31 * result + Arrays.hashCode(parameterSignatures);
        return result;
    }

    @Override
    public String toString() {
        return "Params{" +
                "jmxServiceUrl='" + jmxServiceUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", targetName='" + targetName + '\'' +
                ", query='" + query + '\'' +
                ", methodParameters=" + Arrays.toString(parameterValues) +
                ", methodSignatures=" + Arrays.toString(parameterSignatures) +
                '}';
    }
}
