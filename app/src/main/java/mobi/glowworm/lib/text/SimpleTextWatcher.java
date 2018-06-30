/*
 * Copyright (C) 2018 Glowworm Software
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package mobi.glowworm.lib.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * A simple concrete implementation of the {@link TextWatcher} interface that
 * provides no-op implementations of required methods.
 * <p>
 * This allows for simpler, easier to read code in the common use case of only
 * requiring the {@link #afterTextChanged(Editable)} method.
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        // do nothing
    }
}