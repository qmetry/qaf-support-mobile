/*******************************************************************************
 * QMetry Automation Framework provides a powerful and versatile platform to
 * author
 * Automated Test Cases in Behavior Driven, Keyword Driven or Code Driven
 * approach
 * Copyright 2016 Infostretch Corporation
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT
 * OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * You should have received a copy of the GNU General Public License along with
 * this program in the name of LICENSE.txt in the root folder of the
 * distribution. If not, see https://opensource.org/licenses/gpl-3.0.html
 * See the NOTICE.TXT file in root folder of this source files distribution
 * for additional information regarding copyright ownership and licenses
 * of other open source software / files used by QMetry Automation Framework.
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 *******************************************************************************/

package com.qmetry.qaf.automation.step;

import java.time.Duration;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandCodec;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ResponseCodec;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.JSONUtil;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileCommand;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AppiumCommandExecutor;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.windows.WindowsDriver;

/**
 * com.qmetry.qaf.automation.step.AppiumSteps.java
 * 
 * @author chirag.jayswal
 */
@SuppressWarnings("rawtypes")
public final class AppiumSteps {
   public static int PRESS_TIME = 200; // ms
   public static final WaitOptions watiOpt = WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME));
	/**
	 * Swipe from Bottom to Top.
	 */
	public static void swipeUp() {
		PointOption[] points = getXYtoVSwipe();
		new TouchAction(getDriver()).press(points[0]).waitAction(watiOpt)
				.moveTo(points[1]).release().perform();
	}

	/**
	 * Swipe from Top to Bottom.
	 */
	public static void swipeDown() {
		PointOption[] points = getXYtoVSwipe();
		new TouchAction(getDriver()).press(points[1]).waitAction(watiOpt)
				.moveTo(points[0]).release().perform();
	}

	/**
	 * Swipe from Right to Left.
	 */
	public static void swipeLeft() {
		PointOption[] points = getXYtoHSwipe();
		new TouchAction(getDriver()).press(points[0]).waitAction(watiOpt)
				.moveTo(points[1]).release().perform();

	}

	/**
	 * Swipe from Left to Right
	 */
	public static void swipeRight() {
		PointOption[] points = getXYtoHSwipe();
		new TouchAction(getDriver()).press(points[1]).waitAction(watiOpt)
				.moveTo(points[0]).release().perform();
	}

	/**
	 * @return start and end points for vertical(top-bottom) swipe
	 */
	private static PointOption[] getXYtoVSwipe() {
		// Get screen size.
		Dimension size = getDriver().manage().window().getSize();

		// Find x which is in middle of screen width.
		int startEndx = size.width / 2;
		// Find starty point which is at bottom side of screen.
		int starty = (int) (size.height * 0.70);
		// Find endy point which is at top side of screen.
		int endy = (int) (size.height * 0.30);
		//return PointOption.
		return new PointOption[]{PointOption.point(startEndx, starty), PointOption.point(startEndx, endy)};
	}

	/**
	 * @return start and end points for horizontal(left-right) swipe
	 */
	private static PointOption[] getXYtoHSwipe() {
		// Get screen size.
		Dimension size = getDriver().manage().window().getSize();

		// Find starting point x which is at right side of screen.
		int startx = (int) (size.width * 0.70);
		// Find ending point x which is at left side of screen.
		int endx = (int) (size.width * 0.30);
		// Find y which is in middle of screen height.
		int startEndy = size.height / 2;

		return new PointOption[]{PointOption.point(startx, startEndy), PointOption.point(endx, startEndy)};
	}
	/**
	 * Press and hold the at an absolute position on the screen until the
	 * context menu event has fired.
	 *
	 * @param x
	 *            x coordinate.
	 * @param y
	 *            y coordinate.
	 * @param duration
	 *            of the long-press, in milliseconds.
	 */
	@QAFTestStep(stepName = "longPress", description = "longPress on {x} {y} for {duration} duration")
	public static void longPress(int x, int y, int duration) {
		MultiTouchAction multiTouch = new MultiTouchAction(getDriver());

		for (int i = 0; i < 1; i++) {
			TouchAction tap = new TouchAction(getDriver());
			multiTouch.add(tap.press(PointOption.point(x, y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration))).release());
		}
		multiTouch.perform();
	}

	/**
	 * Creates the swiping action. It is supposed to be performed inside the
	 * given element.
	 *
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param duration
	 *            of the swipe, in milliseconds.
	 */
	@QAFTestStep(stepName = "swipe", description = "swipe from {startX},{startY} to {endX},{endY} in {duration} duration")
	public static void swipe(int startX, int startY, int endX, int endY, int duration) {
		new TouchAction(getDriver()).press(PointOption.point(startX, startY)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
				.moveTo(PointOption.point(endX, endY)).release().perform();
	}

	@SuppressWarnings("unchecked")
	public static AppiumDriver<WebElement> getDriver() {
		WebDriver driver = new WebDriverTestBase().getDriver().getUnderLayingDriver();
		if (driver instanceof AppiumDriver)
			return (AppiumDriver<WebElement>) new WebDriverTestBase().getDriver().getUnderLayingDriver();
		if (driver instanceof QAFExtendedWebDriver) {
			try {
				Capabilities capabilities = ((QAFExtendedWebDriver) driver).getCapabilities();
				HttpCommandExecutor executor = (HttpCommandExecutor) ((QAFExtendedWebDriver) driver)
						.getCommandExecutor();
				AppiumCommandExecutor appiumCommandExecutor = new AppiumCommandExecutor(MobileCommand.commandRepository,
						executor.getAddressOfRemoteServer()) {
					public Response execute(Command command) throws WebDriverException {
						if (DriverCommand.NEW_SESSION.equals(command.getName())) {
							setCommandCodec((CommandCodec<HttpRequest>) ClassUtil.getField("commandCodec", executor));
							setResponseCodec((ResponseCodec<HttpResponse>) ClassUtil.getField("responseCodec", executor));
							getAdditionalCommands().forEach(this::defineCommand);

							Response response = new Response(((QAFExtendedWebDriver) driver).getSessionId());
							response.setValue(JSONUtil.toMap(JSONUtil.toString(capabilities.asMap())));
							response.setStatus(ErrorCodes.SUCCESS);
							response.setState(ErrorCodes.SUCCESS_STRING);
							return response;
						} else {
							return super.execute(command);
						}
					}
				};
				if (capabilities.getPlatform().is(Platform.ANDROID)) {
					return new AndroidDriver<WebElement>(appiumCommandExecutor, capabilities);
				}
				if (capabilities.getPlatform().is(Platform.IOS)) {
					return new IOSDriver<WebElement>(appiumCommandExecutor, capabilities);
				}
				if (capabilities.getPlatform().is(Platform.WINDOWS)) {
					return new WindowsDriver<WebElement>(executor, capabilities);
				}
			} catch (Exception e) {
				throw new AutomationError("Unable to build AppiumDriver from " + driver.getClass(), e);
			}
		}
		throw new AutomationError("Could not retrieve AppiumDriver from " +driver.getClass());
	}

}
