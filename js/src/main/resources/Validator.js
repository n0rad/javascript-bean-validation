Validator = function() {
	var $this = this;

	this.registerConstraint = function(type, func) {
		$this._constraintValidators[type] = func;
	};

	this.validateValue = function(property, propertyDescriptor, subProperty, groups) {
		var violation = [];
		$this._validatePropertyRec(violation, property, propertyDescriptor, property, property, "", subProperty);
		return violation;
	}
	
	this.validate = function(property, propertyDescriptor, groups) {
		var violation = [];
		$this._validateRec(violation, property, propertyDescriptor, property, property);
		return violation;
	};
	
	this.getPropertyDescriptorFromPath = function(rootPropertyDescriptor, propertyPath) {
		var flag = false;
		var properties = propertyPath.split('.');
		var currentPropDesc = rootPropertyDescriptor;
		for (var i = 0; i < properties.length; i++) {
			var property = properties[i].replace(/\[.*?\]/g, '');
			currentPropDesc = currentPropDesc.properties[property];
			if (!currentPropDesc) {
				throw "property not found : " + property; 
			}
		}
		return currentPropDesc;
	};
	
	/////////////////////////////////////////////////////////////
	
	this._validateRec = function(violation, property, propertyDescriptor, rootProperty, previousProperty, path) {
		for (var key in propertyDescriptor.properties) {
			this._validatePropertyRec(violation, property, propertyDescriptor, rootProperty, previousProperty, path, key);
		}
	};
	
	this._validatePropertyRec = function(violation, property, propertyDescriptor, rootProperty, previousProperty, path, key) {
		var ePropertyDescriptor = propertyDescriptor.properties[key];
		var eproperty = property ? property[key] : undefined;
		var epath = path ? path + '.' + key : key;
		$this._validateConstraints(violation, eproperty, ePropertyDescriptor, rootProperty, previousProperty, epath);
		if (ePropertyDescriptor.type == 'array') {
			if ($.isArray(eproperty)) {
				for (var i = 0; i < eproperty.length; i++) {
					var leproperty = eproperty[i];
					var lepath = epath + '[' + i + ']';
					$this._validateRec(violation, leproperty, ePropertyDescriptor, rootProperty, property, lepath);				
				}
			}
		} else {
			$this._validateRec(violation, eproperty, ePropertyDescriptor, rootProperty, property, epath);				
		}		
	}
	
	this._validateConstraints = function(violation, property, propertyDescriptor, rootProperty, previousProperty, path) {
		if (!propertyDescriptor.constraints) {
			return;
		}
		
		for (var i = 0; i < propertyDescriptor.constraints.length; i++) {
			var constraint = propertyDescriptor.constraints[i];
			if ($this._constraintValidators[constraint.type] != undefined) {
				var valid = $this._constraintValidators[constraint.type](property, constraint.attributes);
				if (!valid) {
					var constraintViolation = {};
					constraintViolation['invalidValue'] = property;
					constraintViolation['clientConstraintDescriptor'] = constraint;
					constraintViolation['leafProperty'] = previousProperty;
					constraintViolation['message'] = "";
					constraintViolation['messageTemplate'] = constraint.attributes.message;
					constraintViolation['rootProperty'] = rootProperty;
					constraintViolation['propertyPath'] = path;
					
					violation.push(constraintViolation);
				}				
			} else {
				alert("Constraint validator not found for : " + constraint.type);
			}
		}		
	};
	
	
	this._constraintValidators = {
			'javax.validation.constraints.AssertFalse' : function(obj, attributes) {
				return obj == undefined || false;
			},
			'javax.validation.constraints.AssertTrue' : function(obj, attributes) {
				return obj == undefined || true;
			},
			'javax.validation.constraints.DecimalMax' : function(obj, attributes) {

			},
			'javax.validation.constraints.DecimalMin' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Digits' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}
				if (!isNaN(parseFloat(obj)) && isFinite(obj)) {
					return true;
				}
			},
			'javax.validation.constraints.Future' : function(obj, attributes) {
				return new Date(obj).getTime() >  new Date().getTime();
			},
			'javax.validation.constraints.Max' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Min' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.NotNull' : function(obj, attributes) {
				return obj != undefined;
			},
			'javax.validation.constraints.Null' : function(obj, attributes) {
				return obj == undefined;
			},
			'javax.validation.constraints.Past' : function(obj, attributes) {
				return new Date(obj).getTime() <  new Date().getTime();				
			},
			'javax.validation.constraints.Pattern' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}
				var reg = new RegExp(attributes.regexp);
				return obj.search(reg) != -1;
			},
			'javax.validation.constraints.Size' : function(obj, attributes) {
				if ( obj == null ) {
					return true;
				}
				var length = obj.length;
				return length >= attributes.min && length <= attributes.max;
			}
	};

};

