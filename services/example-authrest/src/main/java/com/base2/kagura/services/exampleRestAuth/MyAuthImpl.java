/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.base2.kagura.services.exampleRestAuth;

import java.util.*;

public class MyAuthImpl implements MyAuth {
    public String echo(String message) {
        return "Echo processed: " + message;
    }

    // Avoiding adding extra dependencies, so using hash map.
    public Object users() {
        return new ArrayList<Map<String, Object>>()
        {{
            add(new HashMap<String, Object>()
            {{
                put("username","testUser1");
                put("groups", Arrays.asList("group1"));
                put("password","thisMechWillChange");
            }});
            add(new HashMap<String, Object>()
            {{
                put("username","testUser2");
                put("groups", Arrays.asList("group2"));
                put("password","thisMechWillChange");
            }});
        }};
    }

    public Object groups() {
        return new ArrayList<Map<String, Object>>()
        {{
            add(new HashMap<String, Object>()
            {{
                    put("groupname","group1");
                    put("reports", Arrays.asList("fake1"));
                }});
            add(new HashMap<String, Object>()
            {{
                    put("groupname","group2");
                    put("reports", Arrays.asList("fake2"));
                }});
        }};
    }

}