package com.example.ordernow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ordernow.R;

public class BillingSettingTabFragment extends Fragment {
    private EditText LNeditText, FNeditText, PCeditText, SReditText, CITYeditText, MAeditText;
    private Spinner countrySpinner;
    private ImageButton imgBttn;

    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing_setting_tab, container, false);


         LNeditText = view.findViewById(R.id.LNeditText);
         FNeditText = view.findViewById(R.id.FNeditText);
         PCeditText = view.findViewById(R.id.PCeditText);
         SReditText = view.findViewById(R.id.SReditText);
         CITYeditText = view.findViewById(R.id.CITYeditText);
         MAeditText = view.findViewById(R.id.MAeditText);
        countrySpinner= view.findViewById(R.id.countrySpinner);
         imgBttn = view.findViewById(R.id.imageButton);
         saveButton = view.findViewById(R.id.saveBttn);


        // Create a list of items for the spinner, including a hint item
        String[] countries = new String[]{"Select Country","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};


        // Create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, countries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set hint color for the hint item
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    // Set default color for other items
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set hint color for the hint item in the dropdown
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    // Set default color for other items
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };

        // Set the Spinner's adapter to the previously created one
        countrySpinner.setAdapter(adapter);

        // Prevent selection of the hint item
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Optionally show a message or perform an action when hint is selected
                } else {
                    // Handle item selection
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle no selection
            }
        });

        // Initially disable UI elements
        setEditable(false);

        // Enable UI elements when ImageButton is clicked
        imgBttn.setOnClickListener(v -> setEditable(true));
        saveButton.setOnClickListener(v -> setEditable(false) );

        return view;
    }
    // Method to enable or disable UI elements
    private void setEditable(boolean isEditable) {
        LNeditText.setEnabled(isEditable);
        FNeditText.setEnabled(isEditable);
        PCeditText.setEnabled(isEditable);
        SReditText.setEnabled(isEditable);
        CITYeditText.setEnabled(isEditable);
        MAeditText.setEnabled(isEditable);
        countrySpinner.setEnabled(isEditable);
        if(isEditable){
            imgBttn.setVisibility(View.INVISIBLE);
            imgBttn.setEnabled(!isEditable);
            saveButton.setEnabled(isEditable);
            saveButton.setVisibility(View.VISIBLE);
        } else{
            imgBttn.setVisibility(View.VISIBLE);
            imgBttn.setEnabled(true);
            saveButton.setVisibility(View.INVISIBLE);
            saveButton.setEnabled(false);
        }

    }
}
