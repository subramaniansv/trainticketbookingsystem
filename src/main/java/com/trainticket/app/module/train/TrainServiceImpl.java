package com.trainticket.app.module.train;
import java.util.*;

import com.trainticket.app.common.*;

public class TrainServiceImpl implements Service<TrainDTO> {

    TrainRepositoryImpl repository = new TrainRepositoryImpl();

    public TrainDTO create(TrainDTO trainDTO) {
        System.out.println("inside service create");
        return repository.save(trainDTO);
    }

    public TrainDTO findById(Long id) {
        System.out.println("id in service"+id);
        TrainDTO train = repository.findById(id);;

        return train;
    }


        public List<TrainDTO> findByRouteId(Long id) {
        System.out.println("id in service"+id);
        List<TrainDTO> train = repository.findByRouteId(id);;

        return train;
    }

    public List<TrainDTO> findAll() {
				System.out.println("hiiii");
         return repository.findAll();
    
    }

    public TrainDTO update(TrainDTO data) {
        TrainDTO train = null;
        boolean isAvailable = repository.existsById(data.getId());
        if (isAvailable) {
            train = repository.save(data);
        } else {
            System.out.println("Train id not found : " + data.getId());
        }

        return train;
    }

    public void delete(Long id){
        boolean isAvailable = repository.existsById(id);
        if (isAvailable) {
          repository.deleteById(id);
        } else {
            System.out.println("Train id not found : " + id);
        }
    }
}
