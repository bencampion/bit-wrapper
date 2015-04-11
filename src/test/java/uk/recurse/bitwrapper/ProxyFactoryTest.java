package uk.recurse.bitwrapper;

import javassist.util.proxy.Proxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProxyFactoryTest {

    @Mock
    private MethodHandler methodHandler;

    private final ProxyFactory proxyFactory = new ProxyFactory();

    @Test(expected = IllegalArgumentException.class)
    public void create_wrapConcreteClass_throwsException() {
        proxyFactory.create(methodHandler, String.class);
    }

    @Test
    public void create_wrapInterface_returnsProxy() {
        CharSequence cs = proxyFactory.create(methodHandler, CharSequence.class);
        assertThat(cs, instanceOf(Proxy.class));
    }
}