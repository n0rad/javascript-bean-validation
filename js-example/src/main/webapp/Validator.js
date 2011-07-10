Validator = function() {
	var $this = this;
	
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
				
			},
			'javax.validation.constraints.Future' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Max' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Min' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.NotNull' : function(obj, attributes) {
				return obj != undefined;
			},
			'javax.validation.constraints.Null' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Past' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Pattern' : function(obj, attributes) {
				
			},
			'javax.validation.constraints.Size' : function(obj, attributes) {

			}
	};
	
	this.registerConstraint = function(type, func) {
		$this._constraintValidators[type] = func;
	};

	this.validate = function(bean, beanDesc) {
		var violation = [];
		$this._validateRec(violation, bean, beanDesc, bean, bean, "");
		return violation;
	};

	this._validateRec = function(violation, bean, beanDesc, rootBean, previousBean, path) {
		$this._validateConstraints(violation, bean, beanDesc, rootBean, previousBean, path);
		
		for (var key in beanDesc.elements) {
			var eBeanDesc = beanDesc.elements[key];
			if (bean != undefined) {
				if ($.isArray(bean[key])) {
//					$this._validateConstraints(violation, bean, beanDesc, rootBean, previousBean, path);
					for (var i = 0; i < bean[key].length; i++) {
						var ebean = bean[key][i];
						var epath = path;
						if (epath) {
							epath += '.';
						}
						epath += key + '[' + i + ']';
						$this._validateRec(violation, ebean, eBeanDesc, rootBean, bean, epath);				
					}
				} else {
					var ebean = bean[key];
					var epath = path;
					if (epath) {
						epath += '.';
					}
					epath += key;
					$this._validateRec(violation, ebean, eBeanDesc, rootBean, bean, epath);				

					// TODO manage MAP
				}
			} else {
				var ebean = undefined;
				var epath = path;
				if (epath) {
					epath += '.';
				}
				epath += key;
				$this._validateRec(violation, ebean, eBeanDesc, rootBean, bean, epath);				
			}
		}
	};
	
	this._validateConstraints = function(violation, bean, beanDesc, rootBean, previousBean, path) {
		for (var i = 0; i < beanDesc.constraints.length; i++) {
			var constraint = beanDesc.constraints[i];
			if ($this._constraintValidators[constraint.type] != undefined) {
				var valid = $this._constraintValidators[constraint.type](bean, constraint.attributes);
				if (!valid) {
					var constraintViolation = {};
					constraintViolation['invalidValue'] = bean;
					// TODO change to ConstraintDescriptor ?
					constraintViolation['beanDesc'] = beanDesc;
					constraintViolation['leafBean'] = previousBean;
					constraintViolation['message'] = "";
					constraintViolation['messageTemplate'] = constraint.attributes.message;
					constraintViolation['rootBean'] = rootBean;
					constraintViolation['propertyPath'] = path;
					
					violation.push(constraintViolation);
				}				
			} else {
				alert("Constraint validator not found for : " + constraint.type);
			}
		}		
	};
};

