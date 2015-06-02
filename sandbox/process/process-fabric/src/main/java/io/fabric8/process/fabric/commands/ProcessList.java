/**
 *  Copyright 2005-2014 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.process.fabric.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import io.fabric8.process.fabric.ContainerInstallOptions;
import io.fabric8.process.manager.Installation;
import io.fabric8.utils.TablePrinter;
import org.apache.felix.gogo.commands.Command;

/**
 * List all processes
 */
@Command(name = "ps", scope = "process", description = "Lists the currently installed managed processes.")
public class ProcessList extends ContainerProcessCommandSupport {

    @Override
    void doWithAuthentication(String jmxUser, String jmxPassword) throws Exception {
        ContainerInstallOptions options = ContainerInstallOptions.builder()
                .container(getContainerObject())
                .user(jmxUser)
                .password(jmxPassword)
                .build();

        List<Installation> installations = getContainerProcessManager().listInstallations(options);
        printInstallations(installations, System.out);
    }

    protected void printInstallations(List<Installation> installations, PrintStream out) {
        TablePrinter printer = new TablePrinter();
        printer.columns("id", "pid", "name");

        for (Installation installation : installations) {
            String id = installation.getId();
            String pid = "";
            try {
                pid = "" + installation.getActivePid();
            } catch (IOException e) {
                pid = "<unknown>";
            }
            printer.row(id, pid, installation.getName());
        }

        printer.print(out);
    }
}