import net.catstack.polish.PolishNotation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OperatorsTests {
    @Test
    fun plusTest() {
        assertEquals(4f, PolishNotation("2 + 2").resolve())
        assertEquals(4f, PolishNotation("2+2").resolve())
        assertEquals(0f, PolishNotation("-2 + 2").resolve())
        assertEquals(0f, PolishNotation("2 + (-2)").resolve())
        assertEquals(35f, PolishNotation("2 + 5 + 28").resolve())
    }

    @Test
    fun minusTest() {
        assertEquals(0f, PolishNotation("2 - 2").resolve())
        assertEquals(0f, PolishNotation("2-2").resolve())
        assertEquals(-4f, PolishNotation("-2 - 2").resolve())
        assertEquals(4f, PolishNotation("2 - (-2)").resolve())
        assertEquals(-30f, PolishNotation("3 - 5 - 28").resolve())
    }

    @Test
    fun multiplyTest() {
        assertEquals(4f, PolishNotation("2*2").resolve())
        assertEquals(0f, PolishNotation("41 * 531 * 0 * 32 * 1").resolve())
        assertEquals(-4f, PolishNotation("2 * (-2)").resolve())
        assertEquals(-4f, PolishNotation("-2 * 2").resolve())
        assertEquals(300f, PolishNotation("2 * 3 * 50 * 1").resolve())
    }

    @Test
    fun divTest() {
        assertEquals(5f, PolishNotation("15 / 3").resolve())
        assertEquals(351f, PolishNotation("351 / 1").resolve())
        assertEquals(0f, PolishNotation("0 / 2").resolve())
    }

    @Test
    fun operatorsPrecedenceTests() {
        assertEquals(6f, PolishNotation("2 + 2 * 2").resolve())
        assertEquals(6f, PolishNotation("((2 + 2) * 2 * 2^3 - 4) / 10").resolve())
    }
}