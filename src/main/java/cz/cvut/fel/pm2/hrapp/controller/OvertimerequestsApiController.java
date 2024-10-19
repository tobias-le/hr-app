package cz.cvut.fel.pm2.hrapp.controller;


import cz.cvut.fel.pm2.hrapp.api.OvertimerequestsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

@Controller
@RequestMapping("${openapi.hRSystem.base-path:}")
public class OvertimerequestsApiController implements OvertimerequestsApi {

    private final NativeWebRequest request;

    @Autowired
    public OvertimerequestsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
