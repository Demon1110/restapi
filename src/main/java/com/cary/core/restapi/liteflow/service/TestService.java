package com.cary.core.restapi.liteflow.service;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

@Component
public class TestService{
  @Inject
  private FlowExecutor flowExecutor;
  
  public void testConfig(){
    LiteflowResponse response = flowExecutor.execute2Resp("chain1", "arg");
  }
}