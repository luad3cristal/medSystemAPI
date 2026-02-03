package ifba.edu.br.medSystemAPI.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "email-ms")
public interface EmailClient {
	
	@RequestMapping(method = RequestMethod.POST, value ="/email/send")
	public ResponseEntity<EmailDTO> sendEmail(@RequestBody EmailDTO emailDTO);

}
