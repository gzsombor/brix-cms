/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package brix.rmiserver.jackrabbit;

import java.util.Properties;

import javax.security.auth.spi.LoginModule;

import org.apache.jackrabbit.core.config.BeanConfig;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.LoginModuleConfig;

abstract class ExtendedLoginModuleConfig extends LoginModuleConfig
{
    private final LoginModuleConfig delegate;

    public ExtendedLoginModuleConfig(LoginModuleConfig delegate)
    {
        super(new BeanConfig("java.lang.String", new Properties()));
        this.delegate = delegate;
    }

    protected abstract LoginModule newLoginModule();

    @Override
    public LoginModule getLoginModule() throws ConfigurationException
    {
        return newLoginModule();
    }

    public boolean equals(Object arg0)
    {
        return delegate.equals(arg0);
    }

    public ClassLoader getClassLoader()
    {
        return delegate.getClassLoader();
    }

    public String getClassName()
    {
        return delegate.getClassName();
    }

    public Properties getParameters()
    {
        return delegate.getParameters();
    }

    public int hashCode()
    {
        return delegate.hashCode();
    }

    public Object newInstance() throws ConfigurationException
    {
        return newLoginModule();
    }

    public void setClassLoader(ClassLoader classLoader)
    {
        delegate.setClassLoader(classLoader);
    }

    public String toString()
    {
        return delegate.toString();
    }


}