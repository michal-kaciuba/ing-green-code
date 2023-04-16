package mkaciuba.inggreencode.atmservice.resource;

import mkaciuba.inggreencode.atmservice.dto.OrderedTask;
import mkaciuba.inggreencode.atmservice.dto.ServiceTask;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/atms/calculateOrder")
public class CalculateOrderResource {

    @PostMapping
    public List<OrderedTask> calculateOrder(@RequestBody List<ServiceTask> tasks) {
        return tasks
                .stream()
                .sorted(Comparator.comparing(ServiceTask::region).thenComparing(ServiceTask::requestType))
                .map(task -> new OrderedTask(task.region(), task.atmId()))
                .distinct()
                .toList();
    }
}
