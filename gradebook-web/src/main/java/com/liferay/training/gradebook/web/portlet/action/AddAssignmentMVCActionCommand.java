package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.workflow.kaleo.definition.Assignment;
import com.liferay.training.gradebook.exception.AssignmentValidationException;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		immediate =true,
		property = {
			"javax.portlet.name=" + GradebookPortletKeys.GRADEBOOK,
			"mvc.command.name=" +MVCCommandNames.ADD_ASSIGNMENT
			},
	service = MVCActionCommand.class
)
public class AddAssignmentMVCActionCommand extends BaseMVCActionCommand{
	
	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception{
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		ServiceContext serviceContext = ServiceContextFactory.getInstance(Assignment.class.getName(), actionRequest);
		
		String title = ParamUtil.getString(actionRequest, "title", StringPool.BLANK);
		String description = ParamUtil.getString(actionRequest, "description", StringPool.BLANK);
		
		Date dueDate = ParamUtil.getDate(actionRequest, "dueDate", DateFormatFactoryUtil.getSimpleDateFormat("MM-dd-YYYY"));
		
		try {
			
			_assignmentService.addAssignment(
					themeDisplay.getScopeGroupId(),title, description, dueDate, serviceContext);
			sendRedirect(actionRequest, actionResponse);
		}
		catch (AssignmentValidationException ave) {
			ave.printStackTrace();
			actionResponse.setRenderParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);;
		}
		catch(PortalException pe) {
			pe.printStackTrace();
			actionResponse.setRenderParameter("mvcRenderCommandName", MVCCommandNames.EDIT_ASSIGNMENT);
		}
	}
	
	@Reference
	protected AssignmentService _assignmentService;
}
