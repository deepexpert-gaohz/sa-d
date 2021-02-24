/*
 * Project Name: standard-code-base
 * File Name: ObjectConverter.java
 * Class Name: ObjectConverter
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * 
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ideatech.common.converter;

/**
 * Class Name: ObjectConverter
 * 
 * @author SC
 * 
 * @param <F>
 * @param <T>
 */
public interface ObjectConverter<F, T> {

    
    /**
    * Description: convert from domain to dto
    *
    * @param domain
    * @param target
    */
    void convertFromDomain(T domain, F target);

    
    /**
    * Description: convert from dto to domain
    *
    * @param source
    * @param domain
    */
    void convertToDomain(F source, T domain);

}
