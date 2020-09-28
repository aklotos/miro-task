/*
 Copyright 2020 Anton Klimenko

 <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 except in compliance with the License. You may obtain a copy of the License at

 <p>http://www.apache.org/licenses/LICENSE-2.0

 <p>Unless required by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 express or implied. See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.aklimenko.miro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "miro")
public class MiroConfig {

  private final RateLimit rateLimit = new RateLimit();

  public static class RateLimit {
    private int windowSizeMS = 60000; // 1 minute
    private int limitGlobal = -1;
    private int limitListWidgets = -1;
    private int limitReadWidget = -1;
    private int limitCreateWidget = -1;
    private int limitUpdateWidget = -1;
    private int limitDeleteWidget = -1;

    public int getWindowSizeMS() {
      return windowSizeMS;
    }

    public void setWindowSizeMS(int windowSizeMS) {
      this.windowSizeMS = windowSizeMS;
    }

    public int getLimitGlobal() {
      return limitGlobal;
    }

    public void setLimitGlobal(int limitGlobal) {
      this.limitGlobal = limitGlobal;
    }

    public int getLimitListWidgets() {
      return limitListWidgets;
    }

    public void setLimitListWidgets(int limitListWidgets) {
      this.limitListWidgets = limitListWidgets;
    }

    public int getLimitReadWidget() {
      return limitReadWidget;
    }

    public void setLimitReadWidget(int limitReadWidget) {
      this.limitReadWidget = limitReadWidget;
    }

    public int getLimitCreateWidget() {
      return limitCreateWidget;
    }

    public void setLimitCreateWidget(int limitCreateWidget) {
      this.limitCreateWidget = limitCreateWidget;
    }

    public int getLimitUpdateWidget() {
      return limitUpdateWidget;
    }

    public void setLimitUpdateWidget(int limitUpdateWidget) {
      this.limitUpdateWidget = limitUpdateWidget;
    }

    public int getLimitDeleteWidget() {
      return limitDeleteWidget;
    }

    public void setLimitDeleteWidget(int limitDeleteWidget) {
      this.limitDeleteWidget = limitDeleteWidget;
    }
  }

  public RateLimit getRateLimit() {
    return rateLimit;
  }
}
