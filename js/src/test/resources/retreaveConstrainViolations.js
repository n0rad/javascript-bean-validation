
function retreaveConstrainViolations(violations) {
	var res = new java.util.HashSet();
	for (var i = 0; i < violations.length; i++) {
		var clientViolation = new fr.norad.client.bean.validation.js.domain.ClientConstraintViolation();
		res.add(clientViolation);
		
		var clientConstraintDescriptor = new fr.norad.client.bean.validation.js.domain.ClientConstraintDescriptor();
		clientConstraintDescriptor.setType(violations[i].clientConstraintDescriptor.type);
		var attributesMap = new java.util.HashMap();
		for (var key in violations[i].clientConstraintDescriptor.attributes) {
			attributesMap.put(key, violations[i].clientConstraintDescriptor.attributes[key]);
		}
		clientConstraintDescriptor.setAttributes(attributesMap);
		
		clientViolation.setClientConstraintDescriptor(clientConstraintDescriptor);
		clientViolation.setMessageTemplate(violations[i].messageTemplate);
		clientViolation.setMessage(violations[i].message);
		clientViolation.setInvalidValue(violations[i].invalidValue);		
		clientViolation.setPropertyPath( violations[i].propertyPath);
	}
	return res;
}