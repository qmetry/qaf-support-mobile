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
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandCodec;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ResponseCodec;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import com.google.common.collect.ImmutableList;
import com.qmetry.qaf.automation.core.AutomationError;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.util.ClassUtil;
import com.qmetry.qaf.automation.util.JSONUtil;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileCommand;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AppiumCommandExecutor;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.windows.WindowsDriver;

/**
 * com.qmetry.qaf.automation.step.AppiumSteps.java
 * 
 * @author chirag.jayswal
 */
public final class AppiumSteps {
   public static int PRESS_TIME = 200; // ms
   public static final WaitOptions watiOpt = WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME));
	/**
	 * Swipe from Bottom to Top.
	 */
	public static void swipeUp() {
		Point[] points = getXYtoVSwipe();
		swipe(points[0].x, points[0].y, points[1].x, points[1].y, PRESS_TIME);
	}

	/**
	 * Swipe from Top to Bottom.
	 */
	public static void swipeDown() {
		Point[] points = getXYtoVSwipe();
		swipe(points[1].x, points[1].y, points[0].x, points[0].y, PRESS_TIME);
	}

	/**
	 * Swipe from Right to Left.
	 */
	public static void swipeLeft() {
		Point[] points = getXYtoHSwipe();
		swipe(points[0].x, points[0].y, points[1].x, points[1].y, PRESS_TIME);
	}

	/**
	 * Swipe from Left to Right
	 */
	public static void swipeRight() {
		Point[] points = getXYtoHSwipe();
		swipe(points[1].x, points[1].y, points[0].x, points[0].y, PRESS_TIME);
	}

	/**
	 * @return start and end points for vertical(top-bottom) swipe
	 */
	private static Point[] getXYtoVSwipe() {
		// Get screen size.
		Dimension size = getAppiumDriver().manage().window().getSize();

		// Find x which is in middle of screen width.
		int startEndx = size.width / 2;
		// Find starty point which is at bottom side of screen.
		int starty = (int) (size.height * 0.70);
		// Find endy point which is at top side of screen.
		int endy = (int) (size.height * 0.30);
		//return PointOption.
		return new Point[]{new Point(startEndx, starty), new Point(startEndx, endy)};
	}

	/**
	 * @return start and end points for horizontal(left-right) swipe
	 */
	private static Point[] getXYtoHSwipe() {
		// Get screen size.
		Dimension size = getAppiumDriver().manage().window().getSize();

		// Find starting point x which is at right side of screen.
		int startx = (int) (size.width * 0.70);
		// Find ending point x which is at left side of screen.
		int endx = (int) (size.width * 0.30);
		// Find y which is in middle of screen height.
		int startEndy = size.height / 2;

		return new Point[]{new Point(startx, startEndy), new Point(endx, startEndy)};
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
		
        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence longPress = new Sequence(input, 0);
        longPress.addAction(input.createPointerMove(Duration.ofMillis(duration), Origin.viewport(), x, y));
        longPress.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        longPress.addAction(new Pause(input, Duration.ofMillis(duration)));

        
        longPress.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        getAppiumDriver().perform(ImmutableList.of(longPress));
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
		
		AppiumDriver d = getAppiumDriver();
		boolean isAndroid = d instanceof AndroidDriver;

        PointerInput input = new PointerInput(Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(input, 0);
        swipe.addAction(input.createPointerMove(Duration.ZERO, Origin.viewport(), startX, startY));
        swipe.addAction(input.createPointerDown(MouseButton.LEFT.asArg()));
        if (isAndroid) {
            duration = duration/3;
        } else {
            swipe.addAction(new Pause(input, Duration.ofMillis(duration)));
            duration = 0;
        }
        swipe.addAction(input.createPointerMove(Duration.ofMillis(duration), Origin.viewport(), endX, endY));
        swipe.addAction(input.createPointerUp(MouseButton.LEFT.asArg()));
        d.perform(ImmutableList.of(swipe));
	}

	@QAFTestStep(description = "switch driver to the {contextName} context ")
	public void switchContext(String contextName) {
		((SupportsContextSwitching) getAppiumDriver()).context(contextName);
	}
	
	@SuppressWarnings("unchecked")
	public static AppiumDriver getAppiumDriver() {
		WebDriver driver = new WebDriverTestBase().getDriver().getUnderLayingDriver();
		if (driver instanceof AppiumDriver)
			return (AppiumDriver) new WebDriverTestBase().getDriver().getUnderLayingDriver();
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
				if (capabilities.getPlatformName().is(Platform.ANDROID)) {
					return new AndroidDriver(appiumCommandExecutor, capabilities);
				}
				if (capabilities.getPlatformName().is(Platform.IOS)) {
					return new IOSDriver(appiumCommandExecutor, capabilities);
				}
				if (capabilities.getPlatformName().is(Platform.WINDOWS)) {
					return new WindowsDriver(executor, capabilities);
				}
			} catch (Exception e) {
				throw new AutomationError("Unable to build AppiumDriver from " + driver.getClass(), e);
			}
		}
		throw new AutomationError("Could not retrieve AppiumDriver from " +driver.getClass());
	}

}
