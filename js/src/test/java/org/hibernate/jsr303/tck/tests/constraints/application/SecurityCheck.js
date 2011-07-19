SecurityCheck = function(obj, attributes) {
	if (obj == null) {
		return true;
	}
	
	if (obj.personalNumber == undefined) {
		return false;
	}
	
	return "000000-0000" != obj.personalNumber;
}