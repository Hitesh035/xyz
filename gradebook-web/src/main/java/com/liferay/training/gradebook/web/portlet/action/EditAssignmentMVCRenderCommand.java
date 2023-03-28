package com.liferay.training.gradebook.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException; 
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.exception.NoSuchAssignmentException; 
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentLocalServiceUtil;
import com.liferay.training.gradebook.service.AssignmentService;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys; 
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.osgi.service.component.annotations.Component; 
import org.osgi.service.component.annotations. Reference;

@Component(
		immediate =true,
		property = {
			"javax.portlet.name=" + GradebookPortletKeys.GRADEBOOK,
			"mvc.command.name=" + MVCCommandNames.EDIT_ASSIGNMENT
			},
	service = MVCRenderCommand.class
)
public class EditAssignmentMVCRenderCommand implements MVCRenderCommand{
	

@Override
public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
	Assignment assignment = null;
	
	Long assignmentId = ParamUtil.getLong(renderRequest, "assignmentId",0);
	List<Assignment>  list = AssignmentLocalServiceUtil.getAssignments(0, 5);
	Iterator<Assignment> itr = list.iterator();
	Long assId = 0L;
	while(itr.hasNext()) {
		assId = itr.next().getAssignmentId();
	}
	System.out.println(assId);
	if (assignmentId > 0) {
		try {
			assignment= _assignmentService.getAssignment (assignmentId);
		}
		catch(NoSuchAssignmentException nsae) { 
			System.out.println(assId);
			nsae.printStackTrace();}
		catch(PortalException pe) { 
			System.out.println(assId);
			pe.printStackTrace();}
	}
	
	ThemeDisplay themeDisplay =(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
// Set back icon visible.
	
	PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
	portletDisplay.setShowBackIcon(true);
	String redirect = renderRequest.getParameter("redirect");
	portletDisplay.setURLBack(redirect);
//-Set-assignment-to-the-request-attributes.
	renderRequest.setAttribute("assignment", assignment);
	renderRequest.setAttribute("assignmentClass", Assignment.class);
	
	return "/assignment/edit_assignment.jsp";
	}

	@Reference
	private AssignmentService _assignmentService;
}
