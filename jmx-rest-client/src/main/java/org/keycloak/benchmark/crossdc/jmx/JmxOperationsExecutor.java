package org.keycloak.benchmark.crossdc.jmx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The entry point of the application as well as the only endpoint implementation for the JMX operation execution.
 */
@RestController
@SpringBootApplication
public class JmxOperationsExecutor {

    /**
     * Performs JMX operation based on the POST request. The request should include parameters for performing the JMX operation.
     * @param jmxParameters     the parameters passed for performing JMX operation.
     * @return
     */
    @PostMapping(value="/performJmxOperation")
    String performJmxOperation(@RequestBody Params jmxParameters) {
        Helper helper = Helper.getInstance(jmxParameters.getJmxServiceUrl(), jmxParameters.getUserName(), jmxParameters.getPassword());

        List<String> result = helper.invokeJmxOperation(jmxParameters.getQuery(), jmxParameters.getTargetName(),
                jmxParameters.getParameterValues(), jmxParameters.getParameterSignatures());

        String response = "OK";
        if (!result.isEmpty()) {
            response = result.get(0);
        }
        return response;
    }

    @PostMapping("/logall")
    String showAllMBeans(@RequestBody Params jmxParameters) {
        Helper helper = Helper.getInstance(jmxParameters.getJmxServiceUrl(), jmxParameters.getUserName(), jmxParameters.getPassword());
        helper.printMBeans();
        return "OK";
    }

    @PostMapping("/getAttribute")
    Object getAttribute(@RequestBody Params jmxParameters) {
        Helper helper = Helper.getInstance(jmxParameters.getJmxServiceUrl(), jmxParameters.getUserName(), jmxParameters.getPassword());
        Object[] value = helper.getAttributeValue(jmxParameters.getQuery(), jmxParameters.getTargetName());
        return value[0];
    }

    public static void main(String[] args) {
        SpringApplication.run(JmxOperationsExecutor.class, args);
    }

}


