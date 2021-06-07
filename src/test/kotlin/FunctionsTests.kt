import net.catstack.polish.PolishNotation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FunctionsTests {
    @Test
    fun absTests() {
        assertEquals(2f, PolishNotation("abs(2)").resolve())
        assertEquals(2f, PolishNotation("abs(-2)").resolve())
    }

    @Test
    fun sinTest() {
        assertEquals(0f, PolishNotation("sin(0)").resolve())
        assertEquals(1f, PolishNotation("sin(90)").resolve())
    }

    @Test
    fun cosTest() {
        assertEquals(1f, PolishNotation("cos(0)").resolve())
        assertEquals(0f, PolishNotation("cos(90)").resolve(), 10e-6f)
    }
}