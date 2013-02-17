Object.size = function(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};

Object.clone = function(obj) {
	  var newObj = (obj instanceof Array) ? [] : {};
	  for (i in obj) {
	    if (i == 'clone') continue;
	    if (obj[i] && typeof obj[i] == "object") {
	      newObj[i] = obj[i].clone();
	    } else newObj[i] = obj[i]
	  } return newObj;
	};

Validator = function() {
	var $this = this;
	this.defaultGroup = "javax.validation.groups.Default";

	this.registerConstraint = function(type, func) {
		$this._constraintValidators[type] = func;
	};

	this.validateValue = function(property, propertyDescriptor, subProperty, groups) {
		if (groups == undefined || groups.length == 0) {
			groups = [$this.defaultGroup];
		}
		var violation = [];
		$this._validatePropertyRec(groups, violation, property, propertyDescriptor, property, property, "", subProperty);
		return violation;
	}
	
	this.validate = function(property, propertyDescriptor, groups) {
		if (groups == undefined || groups.length == 0) {
			groups = [$this.defaultGroup];
		}
		var violation = [];
		$this._validateRec(groups, violation, property, propertyDescriptor, property, property);
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
	
	this.interpolate = function(validatorInfo, violation) {
		var prop = Object.clone(violation.clientConstraintDescriptor.attributes);
		var msg = violation.clientConstraintDescriptor.attributes['message'];
		prop[msg.substring(1, msg.length - 1)] = validatorInfo.messages[msg];
		
		var res = violation.messageTemplate;

		var replaced = true;
		while (replaced) {
			var tmp = res;
			for (var val in prop) {
			    res = res.replace(new RegExp('{' + val + '}', 'g'), prop[val]);
			}
			if (tmp == res) {
				replaced = false;
			}
		}
		return res;
	};
	
	/////////////////////////////////////////////////////////////
	
	this._validateRec = function(groups, violation, property, propertyDescriptor, rootProperty, previousProperty, path) {
		$this._validateConstraints(groups, violation, property, propertyDescriptor, rootProperty, previousProperty, path);
		for (var key in propertyDescriptor.properties) {
			this._validatePropertyRec(groups, violation, property, propertyDescriptor, rootProperty, previousProperty, path, key);
		}
	};
	
	this._validatePropertyRec = function(groups, violation, property, propertyDescriptor, rootProperty, previousProperty, path, key) {
		if (propertyDescriptor == null) {
			return;
		}
		var ePropertyDescriptor = propertyDescriptor.properties[key];
		if (ePropertyDescriptor == null) {
			return; // this property is not in the description tree
		}
		var eproperty = property ? property[key] : undefined;
		var epath = path ? path + '.' + key : key;
		if (ePropertyDescriptor.propertyType == 'ARRAY') {
			if ($.isArray(eproperty)) {
				for (var i = 0; i < eproperty.length; i++) {
					var leproperty = eproperty[i];
					var lepath = epath + '[' + i + ']';
					$this._validateRec(groups, violation, leproperty, ePropertyDescriptor, rootProperty, property, lepath);				
				}
			}
		} else {
			$this._validateRec(groups, violation, eproperty, ePropertyDescriptor, rootProperty, property, epath);				
		}		
	}
	
	this._validateConstraints = function(groups, violation, property, propertyDescriptor, rootProperty, previousProperty, path) {
		if (!propertyDescriptor || !propertyDescriptor.constraints) {
			return;
		}
		
		var done = [];
		for (var i = 0; i < propertyDescriptor.constraints.length; i++) {
			var constraint = propertyDescriptor.constraints[i];
			if ($this._constraintValidators[constraint.type] != undefined) {
				if (done.indexOf(constraint.type) == -1) { // skip if this constraint is already done on this element 
					var groupMatch = false;
					for (var j = 0; j < groups.length; j++) {
						if (groups[j] == $this.defaultGroup && constraint.attributes.groups == undefined) {
							// group is default and constraint have no group
							groupMatch = true;
							break;
						}
						if (constraint.attributes.groups != undefined && constraint.attributes.groups.indexOf(groups[j]) != -1) {
							// group found for this constraint
							groupMatch = true;
							break;
						}
					}
					if (!groupMatch) {
						continue;
					}

					var errorAsSingle = false;
					if (constraint.composingConstraints) {
						for (var j = 0; j < constraint.composingConstraints.length; j++) {
							var current = constraint.composingConstraints[j]
							var valid = $this._constraintValidators[current.type](property, current.attributes);
							if (!valid) {
								if (constraint.reportAsSingle) {
									errorAsSingle = true;
									break;
								}
								var cv = {};
								cv['invalidValue'] = property;
								cv['clientConstraintDescriptor'] = current;
								cv['leafProperty'] = previousProperty;
//								cv['message'] = "";
								cv['messageTemplate'] = current.attributes.message;
								cv['rootProperty'] = rootProperty;
								cv['propertyPath'] = path;								
								violation.push(cv);								
							}
							done.push(constraint.type);
						}
					}
					var valid = $this._constraintValidators[constraint.type](property, constraint.attributes);
					if (!valid || errorAsSingle) {
						var cv = {};
						cv['invalidValue'] = property;
						cv['clientConstraintDescriptor'] = constraint;
						cv['leafProperty'] = previousProperty;
//						cv['message'] = "";
						cv['messageTemplate'] = constraint.attributes.message;
						cv['rootProperty'] = rootProperty;
						cv['propertyPath'] = path;
						
						violation.push(cv);
					}
					done.push(constraint.type);
				}
			} else {
				//TODO manage it
				alert("Constraint validator not found for : " + constraint.type);
			}
		}		
	};
	
	this._constraintValidators = {
			'javax.validation.constraints.AssertFalse' : function(obj, attributes) {
				return obj == undefined || !obj;
			},
			'javax.validation.constraints.AssertTrue' : function(obj, attributes) {
				return obj == undefined || obj;
			},
			'javax.validation.constraints.DecimalMax' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}

				if (!isNaN(parseFloat(obj)) && isFinite(obj)) {
					// number
					return obj <= attributes.value;
				} else {
					return obj.length <= attributes.value;
				}
			},
			'javax.validation.constraints.DecimalMin' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}

				if (!isNaN(parseFloat(obj)) && isFinite(obj)) {
					// number
					return obj >= attributes.value;
				} else {
					return obj.length >= attributes.value;
				}				
			},
			'javax.validation.constraints.Digits' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}
				
				var split = (obj + "").split('.', 2);
				if (attributes.integer != undefined && split[0].length > attributes.integer) {
					return false;
				}
				if (attributes.fraction != undefined && split[1] && split[1].length > attributes.fraction) {
					return false;
				}
				return true;
			},
			'javax.validation.constraints.Future' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}
				return new Date(obj).getTime() >  new Date().getTime();
			},
			'javax.validation.constraints.Max' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}

				if (!isNaN(parseFloat(obj)) && isFinite(obj)) {
					// number
					return obj <= attributes.value;
				} else {
					return obj.length <= attributes.value;
				}
			},
			'javax.validation.constraints.Min' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}

				if (!isNaN(parseFloat(obj)) && isFinite(obj)) {
					// number
					return obj >= attributes.value;
				} else {
					return obj.length >= attributes.value;
				}				
			},
			'javax.validation.constraints.NotNull' : function(obj, attributes) {
				return obj != undefined;
			},
			'javax.validation.constraints.Null' : function(obj, attributes) {
				return obj == undefined;
			},
			'javax.validation.constraints.Past' : function(obj, attributes) {
				if (obj == undefined) {
					return true;
				}
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
				
				var length;
				if (typeof(obj) == 'object') {
					length = Object.size(obj);					
				} else {
					length = obj.length;
				}
				
				return length >= attributes.min && length <= attributes.max;
			}
	};

};
