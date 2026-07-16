package org.safa.maintenanceservice.controller.labor;

import org.safa.maintenanceservice.service.labor.LaborService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/labor")
public class LaborController {
    @Autowired
    private LaborService laborService;
}
