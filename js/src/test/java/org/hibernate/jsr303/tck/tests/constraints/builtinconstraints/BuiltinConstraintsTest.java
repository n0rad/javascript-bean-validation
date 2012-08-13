package org.hibernate.jsr303.tck.tests.constraints.builtinconstraints;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import net.awired.client.bean.validation.js.domain.ClientConstraintViolation;
import net.awired.client.validation.jsr303.tck.TestUtil;
import net.awired.client.validation.tools.ClientValidationTestHelper;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.junit.Test;

public class BuiltinConstraintsTest {

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "c") })
    public void testNullConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        NullDummyEntity dummy = new NullDummyEntity();
        Object foo = new Object();
        dummy.setProperty(foo);
        Set<ConstraintViolation<NullDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);

        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), NullDummyEntity.class, foo, "property");

        dummy.setProperty(null);
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "d") })
    public void testNotNullConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        NotNullDummyEntity dummy = new NotNullDummyEntity();
        Set<ConstraintViolation<NotNullDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), NotNullDummyEntity.class, null, "property");

        dummy.setProperty(new Object());
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "e") })
    public void testAssertTrueConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        AssertTrueDummyEntity dummy = new AssertTrueDummyEntity();

        Set<ConstraintViolation<AssertTrueDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), AssertTrueDummyEntity.class, false, "primitiveBoolean");

        dummy.setPrimitiveBoolean(true);
        dummy.setObjectBoolean(Boolean.FALSE);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), AssertTrueDummyEntity.class, Boolean.FALSE, "objectBoolean");

        dummy.setObjectBoolean(Boolean.TRUE);
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "f") })
    public void testAssertFalseConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        AssertFalseDummyEntity dummy = new AssertFalseDummyEntity();
        dummy.setPrimitiveBoolean(true);

        Set<ConstraintViolation<AssertFalseDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), AssertFalseDummyEntity.class, true, "primitiveBoolean");

        dummy.setPrimitiveBoolean(false);
        dummy.setObjectBoolean(Boolean.TRUE);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), AssertFalseDummyEntity.class, Boolean.TRUE, "objectBoolean");

        dummy.setObjectBoolean(Boolean.FALSE);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        constraintViolations = validator.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "g") })
    public void testMinConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        MinDummyEntity dummy = new MinDummyEntity();

        Set<ConstraintViolation<MinDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        // only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 4);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "bytePrimitive", "intPrimitive",
                "longPrimitive", "shortPrimitive");

        dummy.intPrimitive = 101;
        dummy.longPrimitive = 1001;
        dummy.bytePrimitive = 111;
        dummy.shortPrimitive = 142;

        dummy.intObject = Integer.valueOf("100");
        dummy.longObject = Long.valueOf("0");
        dummy.byteObject = Byte.parseByte("-1");
        dummy.shortObject = Short.parseShort("3");
        dummy.bigDecimal = BigDecimal.valueOf(100.9);
        dummy.bigInteger = BigInteger.valueOf(100);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 6);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "byteObject", "intObject",
                "longObject", "shortObject", "bigDecimal", "bigInteger");

        dummy.intObject = Integer.valueOf("101");
        dummy.longObject = Long.valueOf("12345");
        dummy.byteObject = Byte.parseByte("102");
        dummy.shortObject = Short.parseShort("111");
        dummy.bigDecimal = BigDecimal.valueOf(101.1);
        dummy.bigInteger = BigInteger.valueOf(101);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "h") })
    public void testMaxConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        MaxDummyEntity dummy = new MaxDummyEntity();

        dummy.intPrimitive = 102;
        dummy.longPrimitive = 1234;
        dummy.bytePrimitive = 102;
        dummy.shortPrimitive = 102;

        Set<ConstraintViolation<MaxDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);

        // only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 4);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "bytePrimitive", "intPrimitive",
                "longPrimitive", "shortPrimitive");

        dummy.intPrimitive = 101;
        dummy.longPrimitive = 100;
        dummy.bytePrimitive = 99;
        dummy.shortPrimitive = 42;

        dummy.intObject = Integer.valueOf("102");
        dummy.longObject = Long.valueOf("12345");
        dummy.byteObject = Byte.parseByte("111");
        dummy.shortObject = Short.parseShort("1234");
        dummy.bigDecimal = BigDecimal.valueOf(102);
        dummy.bigInteger = BigInteger.valueOf(102);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);

        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 6);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "byteObject", "intObject",
                "longObject", "shortObject", "bigDecimal", "bigInteger");

        dummy.intObject = Integer.valueOf("101");
        dummy.longObject = Long.valueOf("100");
        dummy.byteObject = Byte.parseByte("100");
        dummy.shortObject = Short.parseShort("101");
        dummy.bigDecimal = BigDecimal.valueOf(100.9);
        dummy.bigInteger = BigInteger.valueOf(100);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "i") })
    public void testDecimalMinConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        DecimalMinDummyEntity dummy = new DecimalMinDummyEntity();

        Set<ConstraintViolation<DecimalMinDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        // only the min constraints on the primitive values should fail. Object values re still null and should pass per spec
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 4);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "bytePrimitive", "intPrimitive",
                "longPrimitive", "shortPrimitive");

        dummy.intPrimitive = 101;
        dummy.longPrimitive = 1001;
        dummy.bytePrimitive = 111;
        dummy.shortPrimitive = 142;

        dummy.intObject = Integer.valueOf("100");
        dummy.longObject = Long.valueOf("0");
        dummy.byteObject = Byte.parseByte("-1");
        dummy.shortObject = Short.parseShort("3");
        dummy.bigDecimal = BigDecimal.valueOf(100.9);
        dummy.bigInteger = BigInteger.valueOf(100);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);

        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 6);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "byteObject", "intObject",
                "longObject", "shortObject", "bigDecimal", "bigInteger");

        dummy.intObject = Integer.valueOf("101");
        dummy.longObject = Long.valueOf("12345");
        dummy.byteObject = Byte.parseByte("102");
        dummy.shortObject = Short.parseShort("111");
        dummy.bigDecimal = BigDecimal.valueOf(101.1);
        dummy.bigInteger = BigInteger.valueOf(101);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);

        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "j") })
    public void testDecimalMaxConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        DecimalMaxDummyEntity dummy = new DecimalMaxDummyEntity();

        dummy.intPrimitive = 102;
        dummy.longPrimitive = 1234;
        dummy.bytePrimitive = 102;
        dummy.shortPrimitive = 102;

        Set<ConstraintViolation<DecimalMaxDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        // only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 4);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "bytePrimitive", "intPrimitive",
                "longPrimitive", "shortPrimitive");

        dummy.intPrimitive = 101;
        dummy.longPrimitive = 100;
        dummy.bytePrimitive = 99;
        dummy.shortPrimitive = 42;

        dummy.intObject = Integer.valueOf("102");
        dummy.longObject = Long.valueOf("12345");
        dummy.byteObject = Byte.parseByte("111");
        dummy.shortObject = Short.parseShort("1234");
        dummy.bigDecimal = BigDecimal.valueOf(102);
        dummy.bigInteger = BigInteger.valueOf(102);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 6);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "byteObject", "intObject",
                "longObject", "shortObject", "bigDecimal", "bigInteger");

        dummy.intObject = Integer.valueOf("101");
        dummy.longObject = Long.valueOf("100");
        dummy.byteObject = Byte.parseByte("100");
        dummy.shortObject = Short.parseShort("101");
        dummy.bigDecimal = BigDecimal.valueOf(100.9);
        dummy.bigInteger = BigInteger.valueOf(100);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "k") })
    public void testSizeConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        SizeDummyEntity dummy = new SizeDummyEntity();

        Set<ConstraintViolation<SizeDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);

        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);

        dummy.collection = new HashSet<String>();
        dummy.collection.add("foo");
        dummy.collection.add("bar");

        dummy.string = "";

        dummy.map = new HashMap<String, String>();
        dummy.map.put("key1", "value1");
        dummy.map.put("key2", "value2");

        dummy.integerArray = new Integer[0];

        dummy.intArray = new int[0];

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 5);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "collection", "map", "string",
                "integerArray", "intArray");

        dummy.collection.remove("bar");
        dummy.string = "a";
        dummy.integerArray = new Integer[1];
        dummy.intArray = new int[1];
        dummy.map.remove("key1");
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "l") })
    public void testDigitsConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        DigitsDummyEntity dummy = new DigitsDummyEntity();

        dummy.intPrimitive = 42;
        dummy.longPrimitive = 42;
        dummy.bytePrimitive = 42;
        dummy.shortPrimitive = 42;

        Set<ConstraintViolation<DigitsDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        // only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 4);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "bytePrimitive", "intPrimitive",
                "longPrimitive", "shortPrimitive");

        dummy.intPrimitive = 1;
        dummy.longPrimitive = 1;
        dummy.bytePrimitive = 1;
        dummy.shortPrimitive = 1;

        dummy.intObject = Integer.valueOf("102");
        dummy.longObject = Long.valueOf("12345");
        dummy.byteObject = Byte.parseByte("111");
        dummy.shortObject = Short.parseShort("1234");
        dummy.bigDecimal = BigDecimal.valueOf(102);
        dummy.bigInteger = BigInteger.valueOf(102);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 6);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "byteObject", "intObject",
                "longObject", "shortObject", "bigDecimal", "bigInteger");

        dummy.intObject = Integer.valueOf("1");
        dummy.longObject = Long.valueOf("1");
        dummy.byteObject = Byte.parseByte("1");
        dummy.shortObject = Short.parseShort("1");
        dummy.bigDecimal = BigDecimal.valueOf(1.93);
        dummy.bigInteger = BigInteger.valueOf(5);

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "m") })
    public void testPastConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        PastDummyEntity dummy = new PastDummyEntity();

        Set<ConstraintViolation<PastDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);

        dummy.calendar = cal;
        dummy.date = cal.getTime();

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 2);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "date", "calendar");

        cal.add(Calendar.YEAR, -2);
        dummy.calendar = cal;
        dummy.date = cal.getTime();
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "n") })
    public void testFutureConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        FutureDummyEntity dummy = new FutureDummyEntity();

        Set<ConstraintViolation<FutureDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);

        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        dummy.calendar = cal;
        dummy.date = cal.getTime();

        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 2);
        TestUtil.assertCorrectPropertyPaths(constraintViolations, clientViolations, "date", "calendar");

        cal.add(Calendar.YEAR, 2);
        dummy.calendar = cal;
        dummy.date = cal.getTime();
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6", id = "a"), @SpecAssertion(section = "6", id = "o") })
    public void testPatternConstraint() {
        Validator validator = TestUtil.getValidatorUnderTest();
        PatternDummyEntity dummy = new PatternDummyEntity();

        Set<ConstraintViolation<PatternDummyEntity>> constraintViolations = validator.validate(dummy);
        Set<ClientConstraintViolation> clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);

        dummy.pattern = "ab cd";
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 1);
        TestUtil.assertConstraintViolation(constraintViolations.iterator().next(),
                clientViolations.iterator().next(), PatternDummyEntity.class, "ab cd", "pattern");

        dummy.pattern = "wc 00";
        constraintViolations = validator.validate(dummy);
        clientViolations = ClientValidationTestHelper.validate(dummy);
        TestUtil.assertCorrectNumberOfViolations(constraintViolations, clientViolations, 0);
    }

    class NullDummyEntity {
        @Null
        Object property;

        public Object getProperty() {
            return property;
        }

        public void setProperty(Object property) {
            this.property = property;
        }
    }

    class NotNullDummyEntity {
        @NotNull
        Object property;

        public Object getProperty() {
            return property;
        }

        public void setProperty(Object property) {
            this.property = property;
        }
    }

    class AssertTrueDummyEntity {
        @AssertTrue
        boolean primitiveBoolean;

        @AssertTrue
        Boolean objectBoolean;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean primitiveBoolean) {
            this.primitiveBoolean = primitiveBoolean;
        }

        public Boolean isObjectBoolean() {
            return objectBoolean;
        }

        public void setObjectBoolean(Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }
    }

    class AssertFalseDummyEntity {
        @AssertFalse
        boolean primitiveBoolean;

        @AssertFalse
        Boolean objectBoolean;

        public boolean isPrimitiveBoolean() {
            return primitiveBoolean;
        }

        public void setPrimitiveBoolean(boolean primitiveBoolean) {
            this.primitiveBoolean = primitiveBoolean;
        }

        public Boolean isObjectBoolean() {
            return objectBoolean;
        }

        public void setObjectBoolean(Boolean objectBoolean) {
            this.objectBoolean = objectBoolean;
        }
    }

    class MinDummyEntity {
        @Min(101)
        public BigDecimal bigDecimal;

        @Min(101)
        public BigInteger bigInteger;

        @Min(101)
        public byte bytePrimitive;

        @Min(101)
        public short shortPrimitive;

        @Min(101)
        public int intPrimitive;

        @Min(101)
        public long longPrimitive;

        @Min(101)
        public Byte byteObject;

        @Min(101)
        public Short shortObject;

        @Min(101)
        public Integer intObject;

        @Min(101)
        public Long longObject;
    }

    class MaxDummyEntity {
        @Max(101)
        public BigDecimal bigDecimal;

        @Max(101)
        public BigInteger bigInteger;

        @Max(101)
        public byte bytePrimitive;

        @Max(101)
        public short shortPrimitive;

        @Max(101)
        public int intPrimitive;

        @Max(101)
        public long longPrimitive;

        @Max(101)
        public Byte byteObject;

        @Max(101)
        public Short shortObject;

        @Max(101)
        public Integer intObject;

        @Max(101)
        public Long longObject;
    }

    class DecimalMaxDummyEntity {
        @DecimalMax("101.000000000")
        public BigDecimal bigDecimal;

        @DecimalMax("1.01E+2")
        public BigInteger bigInteger;

        @DecimalMax("101")
        public byte bytePrimitive;

        @DecimalMax("101")
        public short shortPrimitive;

        @DecimalMax("101")
        public int intPrimitive;

        @DecimalMax("101")
        public long longPrimitive;

        @DecimalMax("101")
        public Byte byteObject;

        @DecimalMax("101")
        public Short shortObject;

        @DecimalMax("101")
        public Integer intObject;

        @DecimalMax("101")
        public Long longObject;
    }

    class DecimalMinDummyEntity {
        @DecimalMin("101.000000000")
        public BigDecimal bigDecimal;

        @DecimalMin("1.01E+2")
        public BigInteger bigInteger;

        @DecimalMin("101")
        public byte bytePrimitive;

        @DecimalMin("101")
        public short shortPrimitive;

        @DecimalMin("101")
        public int intPrimitive;

        @DecimalMin("101")
        public long longPrimitive;

        @DecimalMin("101")
        public Byte byteObject;

        @DecimalMin("101")
        public Short shortObject;

        @DecimalMin("101")
        public Integer intObject;

        @DecimalMin("101")
        public Long longObject;
    }

    class SizeDummyEntity {
        @Size(min = 1, max = 1)
        public String string;

        @Size(min = 1, max = 1)
        public Collection<String> collection;

        @Size(min = 1, max = 1)
        public Map<String, String> map;

        @Size(min = 1, max = 1)
        public Integer[] integerArray;

        @Size(min = 1, max = 1)
        public int[] intArray;
    }

    class DigitsDummyEntity {
        @Digits(integer = 1, fraction = 2)
        public BigDecimal bigDecimal;

        @Digits(integer = 1, fraction = 0)
        public BigInteger bigInteger;

        @Digits(integer = 1, fraction = 0)
        public byte bytePrimitive;

        @Digits(integer = 1, fraction = 0)
        public short shortPrimitive;

        @Digits(integer = 1, fraction = 0)
        public int intPrimitive;

        @Digits(integer = 1, fraction = 0)
        public long longPrimitive;

        @Digits(integer = 1, fraction = 0)
        public Byte byteObject;

        @Digits(integer = 1, fraction = 0)
        public Short shortObject;

        @Digits(integer = 1, fraction = 0)
        public Integer intObject;

        @Digits(integer = 1, fraction = 0)
        public Long longObject;
    }

    class PastDummyEntity {
        @Past
        public Calendar calendar;

        @Past
        public Date date;
    }

    class FutureDummyEntity {
        @Future
        public Calendar calendar;

        @Future
        public Date date;
    }

    class PatternDummyEntity {
        @Pattern(regexp = "[a-z][a-z] \\d\\d")
        public String pattern;
    }
}
