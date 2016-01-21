package de.thi.mymusic.mocker;

import javax.faces.context.FacesContext;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mock FacesContext for JUnit tests
 * see: http://stackoverflow.com/questions/4252353/mocking-facescontext
 */
public abstract class ContextMocker extends FacesContext {
    private ContextMocker() {
    }

    private static final Release RELEASE = new Release();

    private static class Release implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            setCurrentInstance(null);
            return null;
        }
    }

    public static FacesContext mockFacesContext() {
        FacesContext context = Mockito.mock(FacesContext.class);
        setCurrentInstance(context);
        Mockito.doAnswer(RELEASE)
                .when(context)
                .release();
        return context;
    }
}