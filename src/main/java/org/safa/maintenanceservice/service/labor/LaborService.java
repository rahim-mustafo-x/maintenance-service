package org.safa.maintenanceservice.service.labor;

import org.safa.maintenanceservice.repository.LaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaborService {
    @Autowired
    private LaborRepository laborRepository;

    /**
    * When creating a labor there is these after registration
    * by default when created:
    * <br/> status:Free
    * <br/> startWorking & endWorking is mandatory to set:
    * <br/> 8:30 || 18:00
    * <br/> image: use image v1/image end point only when creating  (optional to set)
    * */
    public void save(){

    }
}
