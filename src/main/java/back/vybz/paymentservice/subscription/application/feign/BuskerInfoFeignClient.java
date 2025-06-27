package back.vybz.paymentservice.subscription.application.feign;

import back.vybz.paymentservice.common.entity.BaseResponseEntity;
import back.vybz.paymentservice.subscription.vo.response.ResponseBuskerProfileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "busker-info-service", url = "http://localhost:8089")
public interface BuskerInfoFeignClient {

    @GetMapping("/busker-info-service/api/v1/busker/profile/{buskerUuid}")
    BaseResponseEntity<ResponseBuskerProfileVo> getBuskerProfile(@PathVariable String buskerUuid);
}
