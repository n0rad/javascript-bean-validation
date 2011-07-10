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
				if ( obj == null ) {
					return true;
				}
				var length = obj.length;
				return length >= attributes.min && length <= attributes.max;
			}
	};
	
	this.registerConstraint = function(type, func) {
		$this._constraintValidators[type] = func;
	};

	this.validate = function(bean, beanDesc) {
		var violation = [];
		$this._validateRec(violation, bean, beanDesc, bean, bean);
		return violation;
	};

	this._validateRec = function(violation, bean, beanDesc, rootBean, previousBean, path) {
		for (var key in beanDesc.elements) {
			var eBeanDesc = beanDesc.elements[key];
			var ebean = bean ? bean[key] : undefined;
			var epath = path ? path + '.' + key : key;
			$this._validateConstraints(violation, ebean, eBeanDesc, rootBean, previousBean, epath);
			if (eBeanDesc.type == 'array') {
				if ($.isArray(ebean)) {
					for (var i = 0; i < ebean.length; i++) {
						var lebean = ebean[i];
						var lepath = epath + '[' + i + ']';
						$this._validateRec(violation, lebean, eBeanDesc, rootBean, bean, lepath);				
					}
				}
			} else {
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

